package com.liyiwei.basenetwork.filemanager.uploadstream;

import android.text.TextUtils;

import com.sie.mp.vivo.exception.ExceptionCode;
import com.sie.mp.vivo.exception.SNSException;
import com.sie.mp.vivo.http.HttpResponse;
import com.sie.mp.vivo.http.HttpResponseImpl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by wangdongliang on 2017/7/8.
 */

public class UploadBpmFile {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    public static String uploadFile(File file, String fileUploadUrl) {
        if (TextUtils.isEmpty(fileUploadUrl) || file == null) {
            return "error";
        }

        String result = "";
        HttpResponse response = null;

        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(fileUploadUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file.exists()) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                OutputStream outputSteam = null;
                try {
                    outputSteam = conn.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(outputSteam);
                    StringBuffer sb = new StringBuffer();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);
                    /**
                     * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */

                    sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                            + file.getName() + "\"" + LINE_END);
                    sb.append("Content-Type: multipart/form-data; charset="
                            + CHARSET + LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());
                    InputStream is = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        dos.write(bytes, 0, len);
                    }
                    is.close();
                    dos.write(LINE_END.getBytes());
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                            .getBytes();
                    dos.write(end_data);
                    dos.flush();
                    /**
                     * 获取响应码 200=成功 当响应成功，获取响应的流
                     */
                    response = new HttpResponseImpl(conn);
                    result = response.asJSONObject().toString();
                    int responseCode = conn.getResponseCode();
                    if (responseCode < ExceptionCode.SC_OK || (responseCode != ExceptionCode.SC_FOUND && ExceptionCode.SC_MULTIPLE_CHOICES <= responseCode)) {
                        if (responseCode == ExceptionCode.SC_ENHANCE_YOUR_CLAIM || responseCode == ExceptionCode.SC_BAD_REQUEST || responseCode < ExceptionCode.SC_INTERNAL_SERVER_ERROR) {
                            throw new SNSException(response, response.asString());
                        }
                    }
                } finally {
                    try {
                        outputSteam.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SNSException e) {
            e.printStackTrace();
        }

        return result;
    }

}
