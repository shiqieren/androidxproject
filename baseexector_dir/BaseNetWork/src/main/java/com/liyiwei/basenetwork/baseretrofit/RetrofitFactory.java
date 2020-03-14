package com.liyiwei.basenetwork.baseretrofit;


import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liyiwei.basenetwork.BaseNetWorkApplication;
import com.liyiwei.basenetwork.baseretrofit.interceptor.HttpCacheInterceptor;
import com.liyiwei.basenetwork.baseretrofit.interceptor.HttpHeaderInterceptor;
import com.liyiwei.basenetwork.baseretrofit.interceptor.HttpLoggerInterceptor;
import com.liyiwei.basenetwork.baseretrofit.persistentcookiejar.ClearableCookieJar;
import com.liyiwei.basenetwork.baseretrofit.persistentcookiejar.PersistentCookieJar;
import com.liyiwei.basenetwork.baseretrofit.persistentcookiejar.cache.SetCookieCache;
import com.liyiwei.basenetwork.baseretrofit.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.liyiwei.basenetwork.baseretrofit.transform.NullTypeAdapterFactory;
import com.liyiwei.basenetwork.https.SslSocketFactory;
import com.liyiwei.basenetwork.https.UnSafeHostnameVerify;
import com.liyiwei.basenetwork.https.UnSafeTrustManager;
import com.liyiwei.basenetwork.utils.ApiConfig;
import com.liyiwei.basenetwork.utils.AppContextUtils;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 方式1--使用独立service
 * describe：Retrofit+RxJava网络请求封装
 * author ：mp5a5 on 2018/12/28 11：21
 * email：wwb199055@126.com
 * 配置Retrofit（配置网络缓存cache、配置持久cookie免登录）
 */

@SuppressWarnings("ALL")
public class RetrofitFactory {


    private final Retrofit.Builder retrofit;
    private Retrofit build;
    private final OkHttpClient httpClient;
    public OkHttpClient getClient() {
        return httpClient;
    }
//*****************拦截配置方法-成员变量法***************************//
    //Interceptor
    //    Interceptor REWRITE_HEADER_CONTROL_INTERCEPTOR = chain -> {
    //        return chain.proceed(null);
    //    };
//*****************拦截配置方法-成员变量法***************************//
    private RetrofitFactory() {
        /*================== common ==================*/


//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        // cache指定缓存路径,缓存大小100Mb
        File cacheFile = new File(AppContextUtils.getContext().getCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        //cookie
//        ClearableCookieJar cookieJar =
//                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseNetWorkApplication.getApplicationContext()));

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder()
                .readTimeout(ApiConfig.getDefaultTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(ApiConfig.getDefaultTimeout(), TimeUnit.MILLISECONDS)
                // Log信息拦截器
                .addInterceptor(HttpLoggerInterceptor.getLoggerInterceptor())
                //header配置
                .addInterceptor(new HttpHeaderInterceptor())
                //拦截配置方法-成员变量法
//                .addInterceptor(REWRITE_HEADER_CONTROL_INTERCEPTOR)
                .addNetworkInterceptor(new HttpCacheInterceptor())
//                .cookieJar(cookieJar)
                .cache(cache);

        if (ApiConfig.getOpenHttps()) {
            httpClientBuilder.sslSocketFactory(1 == ApiConfig.getSslSocketConfigure().getVerifyType() ?
                    SslSocketFactory.getSSLSocketFactory(ApiConfig.getSslSocketConfigure().getCertificateInputStream()) :
                    SslSocketFactory.getSSLSocketFactory(), new UnSafeTrustManager());
            httpClientBuilder.hostnameVerifier(new UnSafeHostnameVerify());
            //                           .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }

         httpClient = httpClientBuilder.build();

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
