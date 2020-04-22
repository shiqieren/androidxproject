package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.support.annotation.Nullable;

import com.sie.mp.app.IMApplication;
import com.sie.mp.app.URLConstants;
import com.sie.mp.data.MpFiles;
import com.sie.mp.http3.RetrofitFactory;
import com.sie.mp.http3.RxHelper;
import com.sie.mp.http3.RxSubscribe;
import com.squareup.okhttp.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

public class UploadRequest {

	private static HttpClient httpClient = null;
	private final static int DEFAULT_MAX_CONNECTIONS = 6;
	private final static int DEFAULT_SOCKET_TIMEOUT = 40000;
	private final static int DEFAULT_HOST_CONNECTIONS = 6;

	public static synchronized void stop() {
		getHttpClient().getConnectionManager().shutdown();
	}

	private static synchronized HttpClient getHttpClientEx() {
		if (httpClient == null) {

			HttpParams httpParams = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
			HttpConnectionParams.setSoTimeout(httpParams, 30000);

			ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
					new ConnPerRouteBean(25));

			HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
					httpParams, registry);

			httpClient = new DefaultHttpClient(cm, httpParams);

		}
		return httpClient;
	}

	private static synchronized HttpClient getHttpClient() {

		if (httpClient == null) {
			final HttpParams httpParams = new BasicHttpParams();

			// timeout: get connections from connection pool
			ConnManagerParams.setTimeout(httpParams, 1000);

			// timeout: connect to the server
			HttpConnectionParams.setConnectionTimeout(httpParams,
					DEFAULT_SOCKET_TIMEOUT);

			// timeout: transfer data from server
			HttpConnectionParams.setSoTimeout(httpParams,
					DEFAULT_SOCKET_TIMEOUT);

			// set max connections per host
			ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
					new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));

			// set max total connections
			ConnManagerParams.setMaxTotalConnections(httpParams,
					DEFAULT_MAX_CONNECTIONS);

			// use expect-continue handshake
			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			// disable stale check
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

			HttpClientParams.setRedirecting(httpParams, false);

			// set user agent
			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
			HttpProtocolParams.setUserAgent(httpParams, userAgent);

			// disable Nagle algorithm
			HttpConnectionParams.setTcpNoDelay(httpParams, true);

			HttpConnectionParams.setSocketBufferSize(httpParams, 1024);

			// scheme: http and https
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					httpParams, schemeRegistry);
			httpClient = new DefaultHttpClient(manager, httpParams);
		}

		return httpClient;
	}

	// 上传文件块
	public static long uploadBlock(BlockReader reader, long fileId, int blockNo)
			throws ClientProtocolException, IOException {
		byte[] data = reader.readBlock(blockNo, fileId);
		if(uploadBlock(data)){
			return data.length;
		}else{
			return -1;
		}
	}

	// 上传文件块
	private static boolean uploadBlock(byte[] data)
			throws ClientProtocolException, IOException {
		boolean result = true;
		HttpPost post = new HttpPost(URLConstants.REMOTE_HOST
				+ UploadConstants.UPLOAD_STREAM_ACTION);

		InputStream stream = new ByteArrayInputStream(data);
		InputStreamEntity ety = new InputStreamEntity(stream, -1);
		ety.setContentType("binary/octet-stream");
		ety.setChunked(true);
		post.setEntity(ety);

		try {
			HttpResponse response = getHttpClient().execute(post);
			int code = response.getStatusLine().getStatusCode();
			if(code == 200){
				String str = EntityUtils.toString(response.getEntity());
				result = true;
			}else{
				result = false;
			}
		} catch (ConnectTimeoutException cte) {
            System.out.println("uploadBlock ConnectTimeoutException");
            result = false;
            cte.printStackTrace();
        } catch (SocketTimeoutException ste) {
            System.out.println("uploadBlock SocketTimeoutException");
            result = false;
            ste.printStackTrace();
        } catch (Exception e) {
        	result = false;
			throw new RuntimeException(e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return result;
	}

	/** Returns a new request body that transmits {@code content}. */
	public static RequestBody create(final @Nullable MediaType contentType, final byte[] content,
                                     final int offset, final int byteCount) {
		if (content == null) throw new NullPointerException("content == null");
		Util.checkOffsetAndCount(content.length, offset, byteCount);
		return new RequestBody() {
			@Override public @Nullable MediaType contentType() {
				return contentType;
			}

			@Override public long contentLength() {
				return byteCount;
			}

			@Override public void writeTo(BufferedSink sink) throws IOException {
				sink.write(content, offset, byteCount);
			}
		};
	}

	/**
	 * 查询是否完成
	 * 
	 * @param fileId
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject queryPendingBlock(long fileId)
			throws ClientProtocolException, IOException, JSONException {

		HttpGet get = new HttpGet(URLConstants.REMOTE_HOST
				+ UploadConstants.UPLOAD_QUERY_PENDING_BLOCKS + "?fileId="
				+ fileId);

		HttpResponse response = getHttpClientEx().execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());

			return new JSONObject(result);
		} else {
			throw new RuntimeException("result is not a avaible json");
		}
	}

	/**
	 * 创建空白文件
	 * 
	 * @param file
	 *            --待添加文件数组
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 */
	public static JSONObject addBlankFile(MpFiles file,boolean isAvatar)
			throws ClientProtocolException, IOException, JSONException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("clientFile/fileName=");
		buffer.append(encode(file.getFileName()));
		buffer.append("&clientFile/fileSize=");
		buffer.append(file.getFileSize());
		buffer.append("&clientFile/sourceId=");
		buffer.append(file.getSourceId());
		buffer.append("&clientFile/sourceCode=");
		buffer.append(file.getSourceCode());
		buffer.append("&clientFile/blockCount=");
		buffer.append(file.getBlockCount());
		String URL = null;

		if(isAvatar){
			try {
				buffer.append("&userCode=" + IMApplication.getInstance().getCurrentUser().getUserCode());
			}catch (Exception e){

			}
			URL = URLConstants.REMOTE_HOST + UploadConstants.ADD_CLIENT_FILE_FOR_AVATAR;
			RetrofitFactory.getVChatApi().addBlankAvatar().compose(RxHelper.VChatTransformer())
					.subscribe(new RxSubscribe<String>(this, false) {
						@Override
						public void onSuccess(String result) throws Exception {

						}
					});

		}else{
			URL = URLConstants.REMOTE_HOST + UploadConstants.ADD_CLIENT_FILE_ACTION;
		}

		HttpGet get = new HttpGet(URL + "?" + buffer.toString());

		HttpResponse response = getHttpClientEx().execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			return new JSONObject(result);
		} else {
			throw new RuntimeException("result is not a avaible json");
		}
	}

	 public static String encode(String value) {
	        String encoded = null;
	        try {
	            encoded = URLEncoder.encode(value, "UTF-8");
	        } catch (UnsupportedEncodingException ignore) {
	        }
	        StringBuffer buf = new StringBuffer(encoded.length());
	        char focus;
	        for (int i = 0; i < encoded.length(); i++) {
	            focus = encoded.charAt(i);
	            if (focus == '*') {
	                buf.append("%2A");
	            } else if (focus == '+') {
	                buf.append("%20");
	            } else if (focus == '%' && (i + 1) < encoded.length()
	                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
	                buf.append('~');
	                i += 2;
	            } else {
	                buf.append(focus);
	            }
	        }
	        return buf.toString();
	    }
	 
	/**
	 * 创建空白文件
	 * 
	 * @param files
	 *            --待添加文件数组
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 */
	public static JSONObject addBlankFile(MpFiles[] files)
			throws ClientProtocolException, IOException, JSONException {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < files.length; i++) {
			buffer.append("clientFile[");
			buffer.append(i);
			buffer.append("]/fileId=");
			buffer.append(files[i].getFileId());

			buffer.append("&clientFile[");
			buffer.append(i);
			buffer.append("]fileName=");
			buffer.append(files[i].getFileName());

			buffer.append("&clientFile[");
			buffer.append(i);
			buffer.append("]/fileSize");
			buffer.append(files[i].getFileSize());

			buffer.append("&clientFile[");
			buffer.append(i);
			buffer.append("]/sourceId=");
			buffer.append(files[i].getSourceId());

			buffer.append("&clientFile[");
			buffer.append(i);
			buffer.append("]/sourceCode=");
			buffer.append(files[i].getSourceCode());

			buffer.append("&clientFile[");
			buffer.append(i);
			buffer.append("]/tagId=");
			buffer.append(files[i].getTagId());

			buffer.append("&clientFile[");
			buffer.append(i);
			buffer.append("]/blockCount=");
			buffer.append(files[i].getBlockCount());
		}

		HttpGet get = new HttpGet(URLConstants.REMOTE_HOST
				+ UploadConstants.ADD_CLIENT_MULIT_FILE_ACTION + "?"
				+ buffer.toString());

		HttpResponse response = getHttpClientEx().execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			return new JSONObject(result);
		} else {
			throw new RuntimeException("result is not a avaible json");
		}
	}

}
