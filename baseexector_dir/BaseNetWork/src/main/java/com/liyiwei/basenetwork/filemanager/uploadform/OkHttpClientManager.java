package com.liyiwei.basenetwork.filemanager.uploadform;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static java.lang.String.valueOf;

public class OkHttpClientManager {

    private static final long READ_TIMEOUT = 180;
    private static final long WRITE_TIMEOUT = 180;
    private static final long CONNECT_TIMEOUT = 60;
    private static OkHttpClient httpClient;

    public synchronized static OkHttpClient getOkHttpClientInstance() {
        if (httpClient == null) {
            synchronized (OkHttpClientManager.class) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);//设置读取超时时间
                builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//设置写的超时时间
                builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//设置连接超时时间
//                SSLSocketFactory sslSocketFactory = HttpsInitUtil.setCertificates(LearningApp.getInstance().getResources().openRawResource(R.raw.vivocrt), LearningApp.getInstance().getResources().openRawResource(R.raw.vivoxyz));
//                builder.sslSocketFactory(sslSocketFactory);
                httpClient = builder.build();
            }
        }
        return httpClient;
    }

    /**
     * 带参数上传文件
     * 根据url，发送异步Post请求(带进度)
     *
     * @param url                       提交到服务器的地址
     * @param filePaths                 完整的上传的文件的路径名
     * @param uiProgressRequestListener 上传进度的监听器
     * @param callback                  OkHttp的回调接口
     */
    public static void uploadFile(String url, Map<String, Object> map, List<String> filePaths, ProgressListener uiProgressRequestListener, Callback callback) {
        Call call = getOkHttpClientInstance().newCall(getRequest(url, map, filePaths, uiProgressRequestListener));
        call.enqueue(callback);
    }

    /**
     * 上传单个文件
     *
     * @param url
     * @param filePath
     * @param uiProgressRequestListener
     * @param callback
     */
    public static void uploadFile(String url, Map<String, Object> map, String filePath, ProgressListener uiProgressRequestListener, Callback callback) {
        List<String> filePaths = new ArrayList<String>();
        filePaths.add(filePath);
        Call call = getOkHttpClientInstance().newCall(getRequest(url, map, filePaths, uiProgressRequestListener));
        call.enqueue(callback);
    }

    /**
     * 通过上传的文件的完整路径生成RequestBody
     *
     * @param filePaths 完整的文件路径
     * @return
     */
    private static RequestBody getRequestBody(List<String> filePaths, Map<String, Object> map) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            try {
                builder.addFormDataPart("file", URLEncoder.encode(file.getName(),"UTF-8"), RequestBody.create(MediaType.parse("application/octet-stream"), file)
                );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                builder.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        return builder.build();
    }

    /**
     * 上传文件
     * 获得Request实例(带进度)
     *
     * @param url                       上传文件到服务器的地址
     * @param filePaths                 完整的文件名（带完整路径）
     * @param uiProgressRequestListener 上传进度的监听器
     * @return
     */
    private static Request getRequest(String url, Map<String, Object> map, List<String> filePaths, ProgressListener uiProgressRequestListener) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(new ProgressRequestBody(getRequestBody(filePaths, map), uiProgressRequestListener));
        return builder.build();
    }

    /**
     * 只上传文件,不上传参数
     * 根据url，发送异步Post请求(不带进度)
     *
     * @param url       提交到服务器的地址
     * @param filePaths 完整的上传的文件的路径名
     * @param callback  OkHttp的回调接口
     */
    public static void uploadFile(String url, List<String> filePaths, Callback callback) {
        Call call = getOkHttpClientInstance().newCall(getRequest(url, filePaths));
        call.enqueue(callback);
    }

    /**
     * 上传文件
     * 获得Request实例(不带进度)
     *
     * @param url       上传文件到服务器的地址
     * @param filePaths 完整的文件名（带完整路径）
     * @return
     */
    private static Request getRequest(String url, List<String> filePaths) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(getRequestBody(filePaths)).tag(url); //设置请求的标记，可在取消时使用
        return builder.build();
    }

    private static RequestBody getRequestBody(List<String> filePaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file)
            );
        }
        return builder.build();
    }

}
