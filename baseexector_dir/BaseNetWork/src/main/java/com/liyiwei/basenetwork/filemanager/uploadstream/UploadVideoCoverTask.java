package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.os.AsyncTask;
import android.provider.MediaStore;

import com.sie.mp.app.FileComm;
import com.sie.mp.app.URLConstants;

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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UploadVideoCoverTask extends AsyncTask<File, String, String> {
	private long serverfileId;//服务器文件id
	
	public UploadVideoCoverTask(long id){
		this.serverfileId = id;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(File... files) {
		try{
		File video = files[0];
		File  videoCover = FileComm.buildVideoThumbnail(video.getAbsolutePath(),600,600,MediaStore.Images.Thumbnails.MICRO_KIND);
		return upload(serverfileId,android.util.Base64.encodeToString(getBytesFromFile(videoCover), android.util.Base64.DEFAULT));
		}catch(Exception e){e.printStackTrace();}
		
		return "E";
	}
	

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
//		ToastWrap.showToast(IMApplication.getInstance(), "result:"+result);
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
			result = post(URLConstants.REMOTE_HOST + UploadConstants.UPLOAD_VIDEO_COVER, map);
			JSONObject jo = new JSONObject(result);
			if(jo.has("msgCode") && jo.get("msgCode").toString().equalsIgnoreCase("S")){
				result = "S";
			}else{
				result = "E";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "E";
		}
		return result;
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
}
