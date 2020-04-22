package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.sie.mp.app.FileComm;
import com.sie.mp.app.IMApplication;
import com.sie.mp.data.AttributeTypeConstants;
import com.sie.mp.data.MpChatHis;
import com.sie.mp.data.MpFiles;
import com.sie.mp.util.ClientIdGen;
import com.sie.mp.util.EventBusConstants;
import com.sie.mp.util.NetworkUtils;
import com.sie.mp.util.RequestCompleteEvent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 上传图片(多张)
 * @author guihong_ke
 * 聊天图片在MainActivity的onEventMainThread(Object param)里面接收处理图片上传的结果，头像在FriendInfoActivity2里的onEventMainThread(UploadNotifyEvent param)接收处理
 *
 */
public class UploadMulitPicturesAsyncTask extends AsyncTask<String, String, List<Map<String,Object>>> {
	private List<String> listFiles;
	private List<Map<String,Object>> listUploadFiles;
//	private MpFiles file;
	private String flag = "";
	private final String UPLOAD_STATE_ERROR = "error";
	private final String UPLOAD_STATE_COMPLETE = "complete";

	public UploadMulitPicturesAsyncTask(List<String> file) {
		this.listFiles = file;
	}

	@Override
	protected List<Map<String,Object>> doInBackground(String... flag) {
		this.flag = flag[0];
		String result = UPLOAD_STATE_ERROR;
		listUploadFiles = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		for(int i = 0;i<listFiles.size();i++){
			if(listFiles == null || listFiles.get(i).equals("")){
				break;
			}
			if(isCancelled()){
				return null;
			}
			final MpFiles mpFile = new MpFiles();
			Date date = new Date();
			long newClientId = ClientIdGen.nextVal(ClientIdGen.KEY_NAME_MPCHATHIS);//GeneratePrimaryKey.getChatClientNewId();
			long newFileClientId=ClientIdGen.nextVal(ClientIdGen.KEY_NAME_MPFILES);
			try
			{
//				IMApplication application= IMApplication.getInstance();
				File newUploadFile =null;
//				if(AttributeTypeConstants.CHAT_TYPE_IMAGE.equals((functiontype)))
					newUploadFile= new File(FileComm.getThumbUploadPath(listFiles.get(i),FileComm.getDefaultMaxImageSize()));
//				else
//					newUploadFile=uploadFile;
				final String fileName=newUploadFile.getName();
				String filePath=newUploadFile.getAbsolutePath();
				int blockSize=BlockReader.getBlockCount(newUploadFile);
				FileInputStream fis = new FileInputStream(newUploadFile);
				final int fileAvailable=fis.available();
				long fileLen=newUploadFile.length();
				
				mpFile.setFileName(fileName);
				mpFile.setFilePath(filePath);
				mpFile.setSourceCode("NOTICE");
				mpFile.setBlockCount(blockSize);
				mpFile.setUploadStatus(UploadConstants.UPLOAD_STATUS_TO_ADD);
				mpFile.setOwnerId(IMApplication.getInstance().getCurrentUser().getUserId());
				mpFile.setSourceId(newClientId);
				mpFile.setClientId(newFileClientId);
				mpFile.setFileSize(fileLen);
				mpFile.setFileId(newFileClientId);
				mpFile.setFileSourcePath(listFiles.get(i));
				mpFile.setAutoUpload(AttributeTypeConstants.MPFILE_AUTO_UPLOAD_N);
				mpFile.setExceptionStatus(AttributeTypeConstants.MPFILE_EXCEPTION_STATUS_NONE);
				if (NetworkUtils.checkNet(IMApplication.getInstance())) {
					addBlankClientFile(mpFile);
					File f = new File(mpFile.getFilePath());
					result = upload(mpFile.getFileId(),
							android.util.Base64.encodeToString(getBytesFromFile(f), android.util.Base64.DEFAULT));
				} else {
//					this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
					mpFile.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
					mpFile.setExceptionStatus("EXCEPTION");
					result = UPLOAD_STATE_ERROR;
				}
			} catch (Exception e) {
//					this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
				mpFile.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
				mpFile.setExceptionStatus("EXCEPTION");
				result = UPLOAD_STATE_ERROR;
				// TODO Auto-generated catch block
			}
		
			map = new HashMap<String, Object>();
			if (result.equals(UPLOAD_STATE_COMPLETE)) {
				mpFile.setCompleteDate(new Date());
				mpFile.setSendBlocks(mpFile.getBlockCount());
				mpFile.setUploadSize(mpFile.getFileSize());
				mpFile.setUploadStatus(UploadConstants.UPLOAD_STATUS_COMPLETE);
				mpFile.setExceptionStatus("NONE");
				map.put("result", true);
			}else{
				mpFile.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
				mpFile.setExceptionStatus("EXCEPTION");
				map.put("result", false);
			}
			try {
				Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
				dao.createOrUpdate(mpFile);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("file", mpFile);
			listUploadFiles.add(map);
		}
		return listUploadFiles;
	}

	@Override
	protected void onPostExecute(List<Map<String,Object>> result) {
		RequestCompleteEvent event = new RequestCompleteEvent();
		event.setWhat(EventBusConstants.EVENT_NOTIFY_SEND_MULIT_PHOTOS);
		event.setFlag(flag);
		event.setObj(listUploadFiles);
		EventBus.getDefault().post(event);
		super.onPostExecute(result);
	}

	public void addBlankClientFile(MpFiles file)
			throws SQLException, ClientProtocolException, IOException, JSONException {
		JSONObject js = UploadRequest.addBlankFile(file,false);
		if (js != null && js.has("clientFile")) {
			JSONObject json = js.getJSONObject("clientFile");
			file.setFileId(json.getLong("fileId"));
			file.setServerPath(js.getString("webPath"));
			file.setUploadSize(0);
			file.setSendBlocks(0);
			file.setUploadStatus(UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
			file.setExceptionStatus("NONE");
//			Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
//			UpdateBuilder builder = dao.updateBuilder();
//			builder.updateColumnValue("FILE_ID", file.getFileId());
//			builder.updateColumnValue("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
//			builder.updateColumnValue("EXCEPTION_STATUS", "NONE");
//			builder.updateColumnValue("SEND_BLOCKS", 0);
//			builder.updateColumnValue("UPLOAD_SIZE", 0);
//			builder.updateColumnValue("SERVER_PATH", file.getServerPath());
//			builder.where().eq("CLIENT_ID", file.getClientId());
//			builder.update();
//			dao.update(file);
		} else {
			file.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
			file.setExceptionStatus("EXCEPTION");
//			this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
		}
	}

	/**
	 * 
	 * @param status
	 * @throws SQLException
	 */
	private void saveFileStatus(String status, String exception) throws SQLException {
//		Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
//		file.setUploadStatus(status);
//		file.setExceptionStatus(exception == null ? "NONE" : exception);
//		dao.update(file);
	}

	/**
	 * 文件转为流
	 * 
	 * @param f
	 * @return
	 */
	public byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * 上传图片
	 * 
	 * @param fileId
	 * @param fileString
	 * @return
	 */
	private String upload(Long fileId, String fileString) {
		String result = null;
		try {
			Map map = new HashMap();
			map.put("content", fileString);
			map.put("fileId", fileId.toString());
			result = post(UploadConstants.REMOTE_HOST + UploadConstants.UPLOAD_SINGLE_PIC, map);
			JSONObject jo = new JSONObject(result);
			//{"errCode":"S","errMsg":"上传成功!"}
			if(jo.has("errCode") && jo.get("errCode").toString().equalsIgnoreCase("S")){
				result = UPLOAD_STATE_COMPLETE;
			}else{
				Log.e("tag", "upload.pic : "+result);
				result = UPLOAD_STATE_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("tag", "upload.pic : "+e.getLocalizedMessage());
			result = UPLOAD_STATE_ERROR;
		}
		return result;
	}

	/**
	 * post请求（Volley请求是小数据量交互，这里不推荐使用）
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		HttpPost post = postForm(url, params);
		body = invoke(httpclient, post);
		Log.e("tag","body : "+ body);
//		httpclient.getConnectionManager().shutdown();
		return body;
	}

	private String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		return body;
	}

	/**
	 * 得到返回的结果
	 * 
	 * @param response
	 * @return
	 */
	private String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		// String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 发送请求
	 * 
	 * @param httpclient
	 * @param httpost
	 * @return
	 */
	private HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = null;
		try {
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,60000);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,60000);
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 格式化参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private HttpPost postForm(String url, Map<String, String> params) {
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return httpost;
	}

	/**
	 * 检查是否有正在上传的图片，若有，则这些图片上传状态置为异常
	 */
	public static void checkUploadingPic(){
		List<MpFiles> list_files= null;
		Dao dao = null;
		try 
		{
			dao=IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
			dao=IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
			Where<MpFiles, String> where = dao.queryBuilder().where();
			list_files = (ArrayList<MpFiles>) where.or(where.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_UPLOAD),
					where.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_ADD)).query();			
			if(list_files != null && list_files.size() > 0){
				for (MpFiles mpFiles : list_files) {
					MpChatHis his = com.sie.mp.data.EntityUtils.getMpChatHisByMpFile(mpFiles.getSourceId());
					if(his != null){
						his.setSendState(AttributeTypeConstants.CHATHIS_SEND_STATE_ERROR);
						com.sie.mp.data.EntityUtils.saveMpChatHis2Database(his);
					}
					if(dao != null){
						mpFiles.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
						mpFiles.setExceptionStatus("EXCEPTION");
					}else{
						dao=IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
						mpFiles.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
						mpFiles.setExceptionStatus("EXCEPTION");
					}
//					new UploadPicturesAsyncTask(mpFiles).execute(mpFiles);
				}
				com.sie.mp.data.EntityUtils.saveListData2DataBase(list_files);
			}
		} 
		catch (SQLException e){}
	}
}
