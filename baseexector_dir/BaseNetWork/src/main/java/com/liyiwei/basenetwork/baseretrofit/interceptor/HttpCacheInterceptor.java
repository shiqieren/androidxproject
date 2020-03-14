package com.liyiwei.basenetwork.baseretrofit.interceptor;

import android.util.Log;

import com.liyiwei.basenetwork.utils.AppContextUtils;
import com.liyiwei.basenetwork.utils.NetworkUtils;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.io.IOException;

/**
 * @author ：mp5a5 on 2018/12/26 16：34
 * @describe：配置缓存的拦截器
 * @email：wwb199055@126.com
 */
@SuppressWarnings("ALL")
public class HttpCacheInterceptor implements Interceptor {
    @Override
    @EverythingIsNonNull
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //没网强制从缓存读取
        if (!NetworkUtils.isConnected(AppContextUtils.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Log.e("-->", "no network");
        }

        Response originalResponse = chain.proceed(request);

        if (NetworkUtils.isConnected(AppContextUtils.getContext())) {
            //有网的时候读接口上的@Headers里的配置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}

//    cache配置  成员变量法
//    Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
//
//        //通过 CacheControl 控制缓存数据
//        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
//        cacheBuilder.maxAge(0, TimeUnit.SECONDS);//这个是控制缓存的最大生命时间
//        cacheBuilder.maxStale(365, TimeUnit.DAYS);//这个是控制缓存的过时时间
//        CacheControl cacheControl = cacheBuilder.build();
//
//        //设置拦截器
//        Request request = chain.request();
//        if (!NetUtils.isNetworkAvailable(MyApp.getContext())) {
//            request = request.newBuilder()
//                    .cacheControl(cacheControl)
//                    .build();
//        }
//
//        Response originalResponse = chain.proceed(request);
//        if (NetUtils.isNetworkAvailable(MyApp.getContext())) {
//            int maxAge = 0;//read from cache
//            return originalResponse.newBuilder()
//                    .removeHeader("Pragma")
//                    .header("Cache-Control", "public ,max-age=" + maxAge)
//                    .build();
//        } else {
//            int maxStale = 60 * 60 * 24 * 28;//tolerate 4-weeks stale
//            return originalResponse.newBuilder()
//                    .removeHeader("Prama")
//                    .header("Cache-Control", "poublic, only-if-cached, max-stale=" + maxStale)
//                    .build();
//        }
//    };
