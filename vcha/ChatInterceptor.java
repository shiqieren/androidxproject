package com.sie.mp.http3;

import android.os.Build;
import android.text.TextUtils;

import com.sie.mp.BuildConfig;
import com.sie.mp.app.IMApplication;
import com.sie.mp.data.MpUsers;
import com.sie.mp.space.utils.VLog;
import com.sie.mp.util.LanguageUtil;
import com.sie.mp.util.NetworkUtils;
import com.sie.mp.util.SharedPreferencesUtils;
import com.sie.mp.util.SharedPreferences_Parameter;
import com.sie.mp.vivo.security.AESUtil;
import com.sie.mp.vivo.security.GZIPUtil;
import com.sie.mp.vivo.util.KeyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
//https://blog.csdn.net/jason_996/article/details/78659019
public class ChatInterceptor implements Interceptor, Constant {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        String authToken = SharedPreferencesUtils.getString(SharedPreferences_Parameter.SR_MP_AUTH_TOKEN, "");
        //不加密的参数列表
        HttpUrl httpUrl = oldRequest.url();

        boolean isLogin;
        String userId = "-1";
        String url = httpUrl.toString();
        String userCode = "";
        String ik;
        if (url.contains("app/login") && !url.contains("app/login/tool")) {
            isLogin = true;
            ik = KeyUtil.getAESKey();
        } else {
            isLogin = false;
            ik = authToken;
            MpUsers user = IMApplication.getInstance().getCurrentUser();
            if (user != null){
                userId = user.getUserId() + "";}
        }

        Set<String> notEncName = httpUrl.queryParameterNames();
        JSONObject ncParams = new JSONObject();
        for (String key : notEncName) {
            String value = httpUrl.queryParameter(key);
            try {
                ncParams.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        VLog.e("retrofit", "nc:" + ncParams);
        JSONObject ecParams = new JSONObject();
        RequestBody body = oldRequest.body();
        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            for (int i = 0; i < formBody.size(); i++) {
                try {
                    String key = formBody.name(i);
                    if (isLogin && key.equals(USER_CODE)) {
                        userCode = formBody.value(i);
                    }

                    ecParams.put(key, formBody.value(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String iv = KeyUtil.getAESVector();
        VLog.e("retrofit", "ik:" + ik);
        VLog.e("retrofit", "unec:" + ecParams);

        String ec = AESUtil.getInstance().encrypt(ecParams.toString(), ik, iv);
        JSONObject object = new JSONObject();

        try {
            VLog.e("retrofit", "ec:" + ec);

            object.put(SIGN, isLogin ? generateLoginSign(userCode) : generateSign(userId));
            object.put(ENCRYPT_CONTENT, ec == null ? "" : ec);
            object.put(NORMAL_CONTENT, ncParams.toString());
            object.put(LANGUAGE_NAME, LanguageUtil.getInstance().getDefaultLan());
            object.put(COMMON, commonMsg());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }

        //获取request的创建者builder

        Request.Builder newRequest = oldRequest.newBuilder();

        //从request中获取headers，通过给定的键url_name

        List<String> headerValues = oldRequest.headers("url_name");

        if (headerValues != null && headerValues.size() > 0) {

            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用

            newRequest.removeHeader(HttpConfig.HEADER_KEY);



            //匹配获得新的BaseUrl

            String headerValue = headerValues.get(0);

            HttpUrl newBaseUrl = null;

            if
            ("user".equals(headerValue)) {

                newBaseUrl = HttpUrl.parse(BASE_URL_USER);

            } else
            if  ("pay".equals(headerValue)) {

                newBaseUrl = HttpUrl.parse(BASE_URL_PAY);

            } else{

                newBaseUrl = oldHttpUrl;

            }



            //从request中获取原有的HttpUrl实例oldHttpUrl

            HttpUrl oldHttpUrl = oldRequest.url();

            //重建新的HttpUrl，修改需要修改的url部分

            HttpUrl newFullUrl = oldHttpUrl

                    .newBuilder()

                    .scheme(newBaseUrl.scheme())

                    .host(newBaseUrl.host())

                    .port(newBaseUrl.port())

                    .build();



            //重建这个request，通过builder.url(newFullUrl).build()；



            VLog.e("retrofit", "url:" + url);
        VLog.e("retrofit", "body:" + object.toString());
        newRequest = oldRequest.url(newFullUrl).build().post(RequestBody.create(MediaType.parse(MEDIA_TYPE), object.toString()));

        Response response = chain.proceed(newRequest.build());
        String rb = response.body().string();
        VLog.e("retrofit", "response:" + rb);
        try {
            JSONObject obj = new JSONObject(rb);
            String isFlag = obj.optString("isFlag", null);
            boolean isGzip = obj.optBoolean("isGzip", false);
            boolean isPage = obj.optBoolean("isPage", false);
            int code = obj.optInt("msgCode", 200);
            String returnMsg = null;
            if (!obj.isNull("returnMsg")) {
                returnMsg = obj.getString("returnMsg");
            }
            //            (oMsg == null ? null : oMsg.toString());
            if (code != 200) {
                returnMsg = null;
            }
            if (!"N".equals(isFlag)) {
                returnMsg = AESUtil.getInstance().decrypt(ik, iv, returnMsg);
            }

            if (isGzip) {
                returnMsg = GZIPUtil.getInstance().uncompress(returnMsg);
            }

            VLog.e("retrofit", "returnMsg:" + returnMsg);
            if (isPage) {
                JSONObject o = new JSONObject(returnMsg);
                Iterator<String> it = o.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    obj.put(key, o.get(key));
                }
                obj.remove("returnMsg");
            } else {
                obj.put("returnMsg", returnMsg);
            }

            JSONObject o = new JSONObject();
            Iterator<String> it = obj.keys();
            while (it.hasNext()) {
                String key = it.next();
                if (!"returnMsg".equals(key) && !"data".equals(key)) {
                    o.put(key, obj.get(key));
                } else {
                    Object oValue = obj.opt(key);
                    if (oValue == null) {
                        o.put("returnMsg", null);
                    } else {
                        String value = obj.getString(key);
                        if (isJSONObject(value)) {
                            o.put("returnMsg", parseAsJSONObject(value));
                        } else if (isJSONArray(value)) {
                            o.put("returnMsg", parseAsJSONArray(value));
                        } else {
                            o.put("returnMsg", obj.get(key));
                        }
                    }
                }
            }
            return response.newBuilder().body(ResponseBody.create(MediaType.parse(MEDIA_TYPE), o.toString())).build();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
        //        return chain.proceed(oldRequest);
    }

    private JSONArray parseAsJSONArray(String json) throws JSONException {
        JSONArray array = new JSONArray(json);
        JSONArray newArra = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            Object ok = array.get(i);
            if (ok == null) {
                newArra.put(ok);
                continue;
            }
            String item = array.getString(i);
            if (isJSONObject(item)) {
                JSONObject o = parseAsJSONObject(item);
                newArra.put(o);
            } else if (isJSONArray(item)) {
                JSONArray array1 = parseAsJSONArray(item);
                newArra.put(array1);
            } else {
                newArra.put(ok);
            }
        }
        return newArra;
    }

    private JSONObject parseAsJSONObject(String json) throws JSONException {
        JSONObject o = new JSONObject(json);
        JSONObject newO = new JSONObject();
        Iterator<String> it = o.keys();
        while (it.hasNext()) {
            String key = it.next();
            Object ok = o.get(key);
            if (ok == null) {
                newO.put(key, ok);
                continue;
            }
            String value = o.getString(key);
            if (isJSONObject(value)) {
                Object o1 = parseAsJSONObject(value);
                newO.put(key, o1);
            } else if (isJSONArray(value)) {
                newO.put(key, parseAsJSONArray(value));
            } else {
                newO.put(key, ok);
            }
        }
        return newO;
    }

    private boolean isJSONArray(String msg) {
        if (msg != null && (msg.startsWith("[\"") || msg.startsWith("[{") || msg.equals("[]"))) {
            try {
                JSONArray array = new JSONArray(msg);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isJSONObject(String msg) {
        if (msg != null && msg.startsWith("{")) {
            try {
                JSONObject array = new JSONObject(msg);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String generateSign(String userId) {
        String sign = "";
        JSONObject obj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(userId)) {
                obj.put(USER_ID, userId);
            }
            obj.put(TIMESTAMP, System.currentTimeMillis());
            //            obj.put("iv", iv);
            obj.put("v", 1);

            VLog.e("retrofit", "unsign:" + obj);
            sign = RSAUtil3.encrypt(obj.toString());
            VLog.e("retrofit", "sign:" + sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sign;
    }

    private String generateLoginSign(String userCode) {
        String sign = "";
        JSONObject obj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(userCode)) {
                obj.put(USER_CODE, userCode);
            }
            obj.put(TIMESTAMP, System.currentTimeMillis());
            VLog.e("retrofit", "unsign:" + obj);
            sign = RSAUtil3.encrypt(obj.toString());
            VLog.e("retrofit", "sign:" + sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sign;
    }

    private String commonMsg() {
        JSONObject common = new JSONObject();
        try {
            String nt = NetworkUtils.getConnectionTypeName(IMApplication.getInstance().getApplicationContext());
            common.put(NETWORK_TYPE, nt == null ? "null" : nt);
            common.put(ANDROID_VER, String.valueOf(Build.VERSION.SDK_INT));
            common.put(ANDROID_NAME, Build.VERSION.RELEASE);
            common.put(PHONE_VENDOR, android.os.Build.MANUFACTURER);
            common.put(PHONE_MODEL, android.os.Build.MODEL);
            common.put(PLATFORM, "ANDROID");//必填
            common.put(APP_VERSION, BuildConfig.VERSION_CODE);
        } catch (Exception e) {
            common = null;
            e.printStackTrace();
        }

        return common != null ? common.toString() : "";
    }
}
