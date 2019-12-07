package com.liyiwei.basenetwork.baseretrofit;


import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liyiwei.basenetwork.baseretrofit.interceptor.HttpCacheInterceptor;
import com.liyiwei.basenetwork.baseretrofit.interceptor.HttpHeaderInterceptor;
import com.liyiwei.basenetwork.baseretrofit.interceptor.HttpLoggerInterceptor;
import com.liyiwei.basenetwork.baseretrofit.transform.NullTypeAdapterFactory;
import com.liyiwei.basenetwork.https.SslSocketFactory;
import com.liyiwei.basenetwork.https.UnSafeHostnameVerify;
import com.liyiwei.basenetwork.https.UnSafeTrustManager;
import com.liyiwei.basenetwork.utils.ApiConfig;
import com.liyiwei.basenetwork.utils.AppContextUtils;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * describe：Retrofit+RxJava网络请求封装
 * author ：mp5a5 on 2018/12/28 11：21
 * email：wwb199055@126.com
 */

@SuppressWarnings("ALL")
public class RetrofitFactory {


    private final Retrofit.Builder retrofit;
    private Retrofit build;

    private RetrofitFactory() {

        // 指定缓存路径,缓存大小100Mb
        File cacheFile = new File(AppContextUtils.getContext().getCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder()
                .readTimeout(ApiConfig.getDefaultTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(ApiConfig.getDefaultTimeout(), TimeUnit.MILLISECONDS)
                .addInterceptor(HttpLoggerInterceptor.getLoggerInterceptor())
                .addInterceptor(new HttpHeaderInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache);

        if (ApiConfig.getOpenHttps()) {
            httpClientBuilder.sslSocketFactory(1 == ApiConfig.getSslSocketConfigure().getVerifyType() ?
                    SslSocketFactory.getSSLSocketFactory(ApiConfig.getSslSocketConfigure().getCertificateInputStream()) :
                    SslSocketFactory.getSSLSocketFactory(), new UnSafeTrustManager());
            httpClientBuilder.hostnameVerifier(new UnSafeHostnameVerify());
        }

        OkHttpClient httpClient = httpClientBuilder.build();
//        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//        okHttpClientBuilder.addInterceptor(loggingInterceptor)
//                           .addInterceptor(new ChatInterceptor())
//                           .addInterceptor(loggingInterceptor1)
//                           .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .registerTypeAdapterFactory(new NullTypeAdapterFactory())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (!TextUtils.isEmpty(ApiConfig.getBaseUrl())) {
            //可配置dev，product，local环境
            build = retrofit.baseUrl(ApiConfig.getBaseUrl()).build();
        }

    }

    private static class RetrofitFactoryHolder {
        private static final RetrofitFactory INSTANCE = new RetrofitFactory();
    }

    public static final RetrofitFactory getInstance() {
        return RetrofitFactoryHolder.INSTANCE;
    }


    /**
     * 根据Api接口类生成Api实体
     *
     * @param clazz 传入的Api接口类
     * @return Api实体类
     */
    public <T> T create(Class<T> clazz) {
        checkNotNull(build, "BaseUrl not init,you should init first!");
        return build.create(clazz);
    }

    /**
     * 根据Api接口类生成Api实体
     *
     * @param baseUrl baseUrl
     * @param clazz   传入的Api接口类
     * @return Api实体类
     */
    public <T> T create(String baseUrl, Class<T> clazz) {
        return retrofit.baseUrl(baseUrl).build().create(clazz);
    }

    private <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
