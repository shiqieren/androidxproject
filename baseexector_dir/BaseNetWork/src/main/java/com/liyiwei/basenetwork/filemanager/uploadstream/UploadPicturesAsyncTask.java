package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.sie.mp.app.FileComm;
import com.sie.mp.app.IMApplication;
import com.sie.mp.app.URLConstants;
import com.sie.mp.data.AttributeTypeConstants;
import com.sie.mp.data.MpChatHis;
import com.sie.mp.data.MpFiles;
import com.sie.mp.util.NetworkUtils;

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
 * 上传图片(整张)
 *
 * @author guihong_ke
 * 聊天图片在MainActivity的onEventMainThread(Object param)里面接收处理图片上传的结果，头像在FriendInfoActivity2里的onEventMainThread(UploadNotifyEvent param)接收处理
 */
public class UploadPicturesAsyncTask extends AsyncTask<MpFiles, String, String> {
    private MpFiles file;
    private final String UPLOAD_STATE_ERROR = "error";
    private final String UPLOAD_STATE_COMPLETE = "complete";

    private boolean isAvatar = false;//true:是上传头像，路径固定，调用接口不一样

    public UploadPicturesAsyncTask(MpFiles file) {
        this.file = file;
    }

    @Override
    protected String doInBackground(MpFiles... file) {
        String result = UPLOAD_STATE_ERROR;
        try {
            if (NetworkUtils.checkNet(IMApplication.getInstance())) {
                if (file[0].getAutoUpload().equals(AttributeTypeConstants.MPFILE_AUTO_UPLOAD_N)) {
                    addBlankClientFile(file[0]);
                    File f = new File(FileComm.getThumbUploadPath(file[0].getFilePath(), FileComm.getDefaultMaxImageSize()));
                    result = upload(file[0].getFileId(),
                            android.util.Base64.encodeToString(getBytesFromFile(f), android.util.Base64.DEFAULT));
                } else {
                    addBlankClientFile(file[0]);
                    File f = new File(file[0].getFilePath());
                    result = upload(file[0].getFileId(),
                            android.util.Base64.encodeToString(getBytesFromFile(f), android.util.Base64.DEFAULT));
                }
            } else {
                this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
                notifyFileStatusChange(
                        UploadConstants.BROADCAST_UPLOAD_ERROR,
                        UploadConstants.ERR_CODE_SERVER_EXCEPTION + "", file[0].getSendBlocks(),
                        this.file.getBlockCount(), this.file.getUploadSize());
                result = UPLOAD_STATE_ERROR;
            }
        } catch (Exception e) {
            try {
                this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
                notifyFileStatusChange(
                        UploadConstants.BROADCAST_UPLOAD_ERROR,
                        UploadConstants.ERR_CODE_SERVER_EXCEPTION + ""
                                + e.getMessage(), this.file.getSendBlocks(),
                        this.file.getBlockCount(), this.file.getUploadSize());
                result = UPLOAD_STATE_ERROR;
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                result = UPLOAD_STATE_ERROR;
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals(UPLOAD_STATE_COMPLETE)) {
            file.setCompleteDate(new Date());
            file.setSendBlocks(file.getBlockCount());
            file.setUploadSize(file.getFileSize());
            try {
                saveFileStatus(UploadConstants.UPLOAD_STATUS_COMPLETE, "NONE");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            notifyFileStatusChange(UploadConstants.BROADCAST_UPLOAD_TO_COMPLETE, "", file.getSendBlocks(),
                    file.getBlockCount(), file.getUploadSize());
        } else {
            try {
                this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
                notifyFileStatusChange(
                        UploadConstants.BROADCAST_UPLOAD_ERROR,
                        UploadConstants.ERR_CODE_SERVER_EXCEPTION + "", this.file.getSendBlocks(),
                        this.file.getBlockCount(), this.file.getUploadSize());
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        super.onPostExecute(result);
    }

    public void addBlankClientFile(MpFiles file)
            throws SQLException, ClientProtocolException, IOException, JSONException {
        JSONObject js = UploadRequest.addBlankFile(file, isAvatar);
        if (js != null && js.has("clientFile")) {
            JSONObject json = js.getJSONObject("clientFile");
            file.setFileId(json.getLong("fileId"));
            file.setServerPath(js.getString("webPath"));
            file.setUploadSize(0);
            file.setSendBlocks(0);
            file.setUploadStatus(UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
            file.setExceptionStatus("NONE");
            Dao dao = IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
            UpdateBuilder builder = dao.updateBuilder();
            builder.updateColumnValue("FILE_ID", file.getFileId());
            builder.updateColumnValue("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_UPLOAD);
            builder.updateColumnValue("EXCEPTION_STATUS", "NONE");
            builder.updateColumnValue("SEND_BLOCKS", 0);
            builder.updateColumnValue("UPLOAD_SIZE", 0);
            builder.updateColumnValue("SERVER_PATH", file.getServerPath());
            builder.where().eq("CLIENT_ID", file.getClientId());
            builder.update();
            dao.update(file);
        } else {
            this.saveFileStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION, "EXCEPTION");
            notifyFileStatusChange(UploadConstants.BROADCAST_UPLOAD_ERROR, "在服务器上创建文件失败", file.getSendBlocks(),
                    file.getBlockCount(), file.getUploadSize());
        }
    }

    /**
     * @param status
     * @throws SQLException
     */
    private void saveFileStatus(String status, String exception) throws SQLException {
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
    private void notifyFileStatusChange(String action, String msg, long param1, long param2, long param3) {
        UploadNotifyEvent evt = new UploadNotifyEvent();
        evt.fileId = this.file.getFileId();
        evt.sourceCode = this.file.getSourceCode();
        evt.sourceId = this.file.getSourceId() + "";
        evt.tagId = this.file.getTagId();
        evt.eventType = action;
        evt.param1 = param1;
        evt.param2 = param2;
        evt.param3 = param3;
        EventBus.getDefault().post(evt);
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
            map.put("fileId", fileId.toString());
            map.put("content", fileString);

            result = post(URLConstants.REMOTE_HOST + UploadConstants.UPLOAD_SINGLE_PIC, map);

            JSONObject jo = new JSONObject(result);
            //{"errCode":"S","errMsg":"上传成功!"}
            if (jo.has("errCode") && jo.get("errCode").toString().equalsIgnoreCase("S")) {
                result = UPLOAD_STATE_COMPLETE;
            } else {
                result = UPLOAD_STATE_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        httpclient.getConnectionManager().shutdown();
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
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, IMApplication.REQUEST_TIMEOUT);
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
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
    public static void checkUploadingPic() {
        List<MpChatHis> list_chats = null;
        Dao dao = null;
        try {
            dao = IMApplication.getInstance().getDaoManager().getDao(MpChatHis.class);
            Where<MpChatHis, String> where = dao.queryBuilder().where();
            list_chats = (ArrayList<MpChatHis>) where.eq("SEND_STATE", AttributeTypeConstants.CHATHIS_SEND_STATE_PENDING).query();

            if (list_chats != null && list_chats.size() > 0) {
                for (MpChatHis chat : list_chats) {
                    chat.setSendState(AttributeTypeConstants.CHATHIS_SEND_STATE_ERROR);
                }
                com.sie.mp.data.EntityUtils.saveListData2DataBase(list_chats);
            }
        } catch (SQLException e) {
        }

        //		List<MpFiles> list_files= null;
        //		Dao daoFile = null;
        //		try
        //		{
        //			daoFile=IMApplication.getInstance().getDaoManager().getDao(MpFiles.class);
        //			Where<MpFiles, String> where = daoFile.queryBuilder().where();
        //			list_files = (ArrayList<MpFiles>) where.or(
        //					where.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_UPLOAD),
        //					where.eq("UPLOAD_STATUS", UploadConstants.UPLOAD_STATUS_TO_ADD)).query();
        //
        //			if(list_files != null && list_files.size() > 0)
        //			{
        //				for (MpFiles mpFiles : list_files)
        //				{
        //					mpFiles.setUploadStatus(UploadConstants.UPLOAD_STATUS_EXCEPTION);
        //				}
        //				com.sie.mp.data.EntityUtils.saveListData2DataBase(list_files);
        //			}
        //		}
        //		catch (SQLException e){}
    }

    public void setAvatar(boolean avatar) {
        isAvatar = avatar;
    }
}
