package com.liyiwei.basenetwork.api;

import android.util.ArrayMap;

import com.liyiwei.basenetwork.baseretrofit.entity.NBAJEntity;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author ：mp5a5 on 2019/1/16 10：08
 * @describe
 * @email：wwb199055@126.com
 */
public interface NbaJavaApi {

    @GET("wxarticle/chapters/json")
    Observable<NBAJEntity> getJONBAInfo(@QueryMap ArrayMap<String, Object> map);

    @GET("wxarticle/chapters/json")
    Flowable<NBAJEntity> getNBAJFTInfo(@QueryMap ArrayMap<String, Object> map);
}
