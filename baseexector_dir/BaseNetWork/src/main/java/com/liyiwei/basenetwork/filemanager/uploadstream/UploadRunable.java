package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.sie.mp.app.FileComm;
import com.sie.mp.app.IMApplication;
import com.sie.mp.app.URLConstants;
import com.sie.mp.data.AttributeTypeConstants;
import com.sie.mp.data.MpFiles;
import com.sie.mp.util.NetworkUtils;
import com.sie.mp.util.SharedPreferencesUtils;
import com.sie.mp.util.SharedPreferences_Parameter;
import com.sie.mp.vivo.util.UploadFileUtil;

import org.apache.http.client.ClientProtocolException;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class UploadRunable implements Runnable {
	private MpFiles file;
	private BlockReader reader = null; // 文件读取Reader

	public UploadRunable(MpFiles file) {
		this.file = file;
	}

	/**
	 * 检查网络链接是否可用
	 * 
	 * @return
	 * @throws SQLException
	 */
	private boolean assertNetwork(boolean throwException) throws SQLException {
		if (!NetworkUtils.checkNet(IMApplication.getInstance())) {
			if (throwException) {
				throw new RuntimeException();
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param status
	 * @throws SQLException
	 */
	private void saveFileStatus(String status, String exception)throws SQLException 
	{
		Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
		file.setUploadStatus(status);
		file.setExceptionStatus(exception == null ? "NONE" : exception);
		dao.update(file);
	}

	/**
	 * 发送广播通知文件状态改变，可能的改变包括添加空白文件成功，上传进度变化、文件全部上传、服务器确认上传完成
	 *
	 * @param msg
	 * @param action
	 * @throws SQLException
	 */
	private void notifyFileStatusChange(String action, String msg, long param1,
			long param2, long param3) {
		UploadNotifyEvent evt = new UploadNotifyEvent();
		evt.fileId = this.file.getFileId();
		evt.sourceCode = this.file.getSourceCode();
		evt.sourceId = this.file.getSourceId() + "";
		evt.tagId = this.file.getTagId();
		evt.filePath=this.file.getFilePath();
		evt.fileType=this.file.getFileType();
		evt.eventType = action;
		evt.param1 = param1;
		evt.param2 = param2;
		evt.param3 = param3;
		EventBus.getDefault().post(evt);
	}
	
	

	@Override
	public void run()
	{
		SharedPreferencesUtils.setInt(IMApplication.getInstance().getCurrentUser().getUserId() + "." + SharedPreferences_Parameter.MP_CHAT_STOP_UPLOAD+"."+file.getSourceId(), 0);
		this.file.setExceptionStatus("NONE");
		this.file.setCompleteDate(null);
		try
		{
			this.saveFileStatus(this.file.getUploadStatus(), "NONE");
			if (UploadConstants.UPLOAD_STATUS_TO_ADD.equalsIgnoreCase(this.file.getUploadStatus()) && 
					this.file.getFileId() == this.file.getClientId()) 
			{
				addBlankClientFile();
			} 
			else if(UploadConstants.UPLOAD_STATUS_EXCEPTION.equalsIgnoreCase(this.file.getUploadStatus()) && 
					this.file.getFileId() == this.file.getClientId())
			{
				addBlankClientFile();
			}
			else 
			{
				notifyFileStatusChange(UploadConstants.BROADCAST_BEGIN_UPLOAD,
						UploadConstants.ERR_CODE_SERVER_EXCEPTION + "",
						this.file.getSendBlocks(), this.file.getBlockCount(),
						this.file.getUploadSize());// 通知开始上传
				this.queryPendingBlocks();
			}
		}catch (Exception e) {
			try {
				saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION,"EXCEPTION");
				notifyFileStatusChange(
						UploadConstants.BROADCAST_UPLOAD_ERROR,
						UploadConstants.ERR_CODE_SERVER_EXCEPTION + ""
								+ e!=null?e.getMessage():"", this.file.getSendBlocks(),
						this.file.getBlockCount(), this.file.getUploadSize());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (this.reader != null) {
					this.reader.close();
					this.reader = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addBlankClientFile() throws SQLException,
			ClientProtocolException, IOException, JSONException {

		JSONObject js = UploadRequest.addBlankFile(this.file,false);
		if (js != null && js.has("clientFile")) 
		{
			JSONObject json = js.getJSONObject("clientFile");
			file.setFileId(json.getLong("fileId"));
			file.setServerPath(js.getString("webPath"));
			file.setUploadSize(0);
			file.setSendBlocks(0);
			file.setUploadStatus(UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
			file.setExceptionStatus("NONE");
			Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
			UpdateBuilder builder = dao.updateBuilder();
			builder.updateColumnValue("FILE_ID", this.file.getFileId());
			builder.updateColumnValue("UPLOAD_STATUS",UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
			builder.updateColumnValue("EXCEPTION_STATUS", "NONE");
			builder.updateColumnValue("SEND_BLOCKS", 0);
			builder.updateColumnValue("UPLOAD_SIZE", 0);
			builder.updateColumnValue("SERVER_PATH", this.file.getServerPath());

			builder.where().eq("CLIENT_ID", this.file.getClientId());
			builder.update();
			//dao.update(file);
			
			//uploadVideoPic(file.getFileId(),file.getFilePath(),file.getFileType());
			
			notifyFileStatusChange(UploadConstants.BROADCAST_ADD_BLANK_FILE,
					null, this.file.getSendBlocks(), this.file.getBlockCount(),
					this.file.getUploadSize());
			this.queryPendingBlocks();
			
		}
		else
		{
			this.saveFileStatus(UploadConstants.UPLOAD_STATUS_TO_ADD,"EXCEPTION");
			notifyFileStatusChange(UploadConstants.BROADCAST_UPLOAD_ERROR,"在服务器上创建文件失败", this.file.getSendBlocks(),this.file.getBlockCount(), this.file.getUploadSize());
		}
	}

	/**
	 * 上传视频截图
	 * **/
	private void uploadVideoPic(long fileId,String filePath,String fileType)
	{
		Log.v("--UploadRunnalbeFile---", "--UploadRunnalbeFile---"+fileId+" "+filePath+" "+fileType);
		File videoFile = new File(filePath);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(fileType!=null&&videoFile.exists() && AttributeTypeConstants.CHAT_TYPE_VIDEO.equals(fileType)){
			  File  videoCover = FileComm.buildVideoThumbnail(videoFile.getAbsolutePath(),600,600,MediaStore.Images.Thumbnails.MICRO_KIND);
			  String result=UploadFileUtil.getInstance().upload(fileId,android.util.Base64.encodeToString(UploadFileUtil.getBytesFromFile(videoCover), android.util.Base64.DEFAULT));
			  Log.v("--UploadRunnalbeFile---", "--UploadRunnalbeFile2---"+fileId+" "+videoCover.length()+" "+result);
			  if(result==null||result.equals("E"))
			  {
				  //上传截屏失败，取消视频上传
				  throw new RuntimeException();
			  }
		}else
		{
			throw new RuntimeException();
		}
		
	}
	
	
	
	/**
	 * 向服务器询问有多少块等待上传
	 * 
	 * @throws SQLException
	 * @throws JSONException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private void queryPendingBlocks() throws SQLException,
			ClientProtocolException, IOException, JSONException {

		// 断言网络
		assertNetwork(true);
		JSONObject response = UploadRequest.queryPendingBlock(this.file.getFileId());
		if (response.has("blocks")) {
			JSONArray blocks = response.getJSONArray("blocks");
			if (blocks != null) {
				int len = blocks.length();
				if (len == 0) {// 如果已经没有块上传
					// 更新本地数据标示为已经上传完毕
					this.file.setCompleteDate(new Date());
					this.file.setSendBlocks(this.file.getBlockCount());
					this.file.setUploadBlocks(this.file.getBlockCount());
					this.file.setUploadSize(this.file.getFileSize());

					saveFileStatus(UploadConstants.UPLOAD_STATUS_COMPLETE,
							"NONE");

					notifyFileStatusChange(
							UploadConstants.BROADCAST_UPLOAD_COMPLETE, "",
							this.file.getSendBlocks(),
							this.file.getBlockCount(),
							this.file.getUploadSize());
				} else {
					int totalBlocks = 0;
					int totalSize = 0;
					int tempBlocks = 0;
					
					long temp=(this.file.getBlockCount()-1)*BlockReader.BLOCK_SIZE;
					
//					long lastBlockSize = (this.file.getBlockCount() * BlockReader.BLOCK_SIZE)
//							% this.file.getFileSize(); // 最后一个块的大小
					long lastBlockSize =this.file.getFileSize()-temp;//最后一个块的大小

					for (int i = 0; i < len; i++) {
						JSONObject obj = blocks.getJSONObject(i);
						tempBlocks = obj.getInt("to") - obj.getInt("from") + 1;
						totalBlocks += tempBlocks;
						if (obj.getInt("to") == this.file.getBlockCount() - 1) {// 如果是最后一个块
							totalSize += (tempBlocks - 1)
									* BlockReader.BLOCK_SIZE;
							totalSize += lastBlockSize;
						} else {
							totalSize += tempBlocks * BlockReader.BLOCK_SIZE;
						}
					}

					this.file.setCompleteDate(null);
					this.file.setSendBlocks(this.file.getBlockCount()
							- totalBlocks); // 已经发送的块数量
					this.file
							.setUploadSize(this.file.getFileSize() - totalSize < 0 ? 0
									: this.file.getFileSize() - totalSize); // 已经上传的文件大小

					for (int i = 0; i < len; i++) {
						JSONObject obj = blocks.getJSONObject(i);
						if(!upload(obj.getInt("from"), obj.getInt("to"))){
							return;
						}
					}
					
					// 传完毕之后更新状态
//					this.file.setCompleteDate(new Date());
//					this.file.setSendBlocks(this.file.getBlockCount());
//					this.file.setUploadSize(this.file.getFileSize());
//
//					saveFileStatus(UploadConstants.UPLOAD_STATUS_COMPLETE,
//							"NONE");
//					notifyFileStatusChange(
//							UploadConstants.BROADCAST_UPLOAD_TO_COMPLETE, "",
//							this.file.getSendBlocks(),
//							this.file.getBlockCount(),
//							this.file.getUploadSize());
//
//					System.gc();

					try {
						JsonObjectRequest req =  new JsonObjectRequest(Method.POST, URLConstants.REMOTE_HOST
								+UploadConstants.UPLOAD_COMPLETED_ACTION
								+ "?fileId=" + this.file.getFileId(), null,
								new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										try {
											boolean isCompleted = response.has("completed")?response.getBoolean("completed"):false;
											if(isCompleted){
												// 传完毕之后更新状态
												file.setCompleteDate(new Date());
												file.setSendBlocks(file.getBlockCount());
												file.setUploadSize(file.getFileSize());
												saveFileStatus(UploadConstants.UPLOAD_STATUS_COMPLETE,"NONE");
												notifyFileStatusChange(
														UploadConstants.BROADCAST_UPLOAD_TO_COMPLETE, "",
														file.getSendBlocks(),
														file.getBlockCount(),
														file.getUploadSize());
												System.gc();
											}else{
												new Thread(new Runnable() {
													public void run() {
														try {
															queryPendingBlocks();
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}
												}).start();
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}, new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
									}
								});
						req.setRetryPolicy(new DefaultRetryPolicy(IMApplication.REQUEST_TIMEOUT, 1, 1.0f));
						IMApplication.getInstance().getHttpManager().add(req);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 上传
	 * 
	 * @param from
	 * @param to
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private boolean upload(int from, int to) throws SQLException,
			ClientProtocolException, IOException {
		for (int i = from; i <= to; i++) {
			
			if(!upload(i)){
				return false;
			}
//			upload(i);
		}
		return true;
	}

	/**
	 * 上传特定的块
	 * 
	 * @param blockNo
	 * @throws SQLException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private boolean upload(int blockNo) throws SQLException,
			ClientProtocolException, IOException {
		//file.getSourceId();
		//判断是否终止上传
		int flag=SharedPreferencesUtils.getInt(IMApplication.getInstance().getCurrentUser().getUserId() + "." + SharedPreferences_Parameter.MP_CHAT_STOP_UPLOAD+"."+file.getSourceId(), 0);
		if(flag==1)
		{
			//归位是否上传标记
			SharedPreferencesUtils.setInt(IMApplication.getInstance().getCurrentUser().getUserId() + "." + SharedPreferences_Parameter.MP_CHAT_STOP_UPLOAD+"."+file.getSourceId(), 0);
			throw new RuntimeException();
		}
		// 判断是否有网络
		assertNetwork(true);

		if (reader == null) {
			this.reader = new BlockReader(file.getFilePath());
		}

		// 上传特定的块
		long result = UploadRequest.uploadBlock(reader, this.file.getFileId(), blockNo);
		int uploadSum = 5;
		if(result == -1){
			for(int i = 0;i<uploadSum;i++){
				if(upload2(blockNo)){
					return true;
				}
				if(i == uploadSum -1){
					saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION,"EXCEPTION");
					notifyFileStatusChange(
							UploadConstants.BROADCAST_UPLOAD_ERROR,
							"文件id为"+file.getFileId()+"上传第"+blockNo+"块失败", this.file.getSendBlocks(),
							this.file.getBlockCount(), this.file.getUploadSize());
					return false;
				}
			}
		}
//		long lastBlockSize = (this.file.getBlockCount() * BlockReader.BLOCK_SIZE)
//				% this.file.getFileSize(); // 最后一个块的大小
		long temp=(this.file.getBlockCount()-1)*BlockReader.BLOCK_SIZE;
		
//		long lastBlockSize = (this.file.getBlockCount() * BlockReader.BLOCK_SIZE)
//				% this.file.getFileSize(); // 最后一个块的大小
		long lastBlockSize =this.file.getFileSize()-temp;//最后一个块的大小
		if (blockNo == this.file.getBlockCount() - 1) {// 如果是最后一个块
			this.file.setUploadSize(this.file.getUploadSize() + lastBlockSize);
		} else {
			this.file.setUploadSize(this.file.getUploadSize()
					+ BlockReader.BLOCK_SIZE);
		}
		if (this.file.getUploadSize() >= this.file.getFileSize()) {
			this.file.setUploadSize(this.file.getFileSize());
		}

		this.file.setSendBlocks(this.file.getSendBlocks() + 1);

		notifyFileStatusChange(UploadConstants.BROADCAST_UPDATE_PROGRESS, "",
				this.file.getSendBlocks(), this.file.getBlockCount(),
				this.file.getUploadSize());
		return true;
	}
	
	/**
	 * 上传特定的块
	 * 
	 * @param blockNo
	 * @throws SQLException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private boolean upload2(int blockNo) throws SQLException,
	ClientProtocolException, IOException {
		// 判断是否有网络
		assertNetwork(true);
		
		if (reader == null) {
			this.reader = new BlockReader(file.getFilePath());
		}
		
		// 上传特定的块
		if(UploadRequest.uploadBlock(reader, this.file.getFileId(), blockNo) == -11){
			return false;
		}
		
		long lastBlockSize = (this.file.getBlockCount() * BlockReader.BLOCK_SIZE)
				% this.file.getFileSize(); // 最后一个块的大小
		
		if (blockNo == this.file.getBlockCount() - 1) {// 如果是最后一个块
			this.file.setUploadSize(this.file.getUploadSize() + lastBlockSize);
		} else {
			this.file.setUploadSize(this.file.getUploadSize()
					+ BlockReader.BLOCK_SIZE);
		}
		if (this.file.getUploadSize() >= this.file.getFileSize()) {
			this.file.setUploadSize(this.file.getFileSize());
		}
		
		this.file.setSendBlocks(this.file.getSendBlocks() + 1);
		
		notifyFileStatusChange(UploadConstants.BROADCAST_UPDATE_PROGRESS, "",
				this.file.getSendBlocks(), this.file.getBlockCount(),
				this.file.getUploadSize());
		return true;
	}
}
