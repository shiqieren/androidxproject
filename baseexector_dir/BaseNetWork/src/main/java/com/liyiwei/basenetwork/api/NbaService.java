package com.liyiwei.basenetwork.api;

import android.util.ArrayMap;

import com.liyiwei.basenetwork.baseretrofit.RetrofitFactory;
import com.liyiwei.basenetwork.baseretrofit.entity.NBAJEntity;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @author ：mp5a5 on 2019/1/16 16：37
 * @describe
 * @email：wwb199055@126.com
 */
public class NbaService {

    private NbaJavaApi nbaApiT;

    private NbaService() {
        nbaApiT = RetrofitFactory.getInstance().create(NbaJavaApi.class);
    }

    public static NbaService  getInstance() {
        return NbaserviceHolder.S_INSTANCE;
    }

    private static class NbaserviceHolder {
        private static final NbaService S_INSTANCE = new NbaService();
    }

    public Observable<NBAJEntity> getJONBAInfo(String key) {
        ArrayMap<String, Object> map = new ArrayMap<>();
//        map.put("key", key);
        return nbaApiT.getJONBAInfo(map);
    }

    public Flowable<NBAJEntity> getJFNBAInfo(String key) {
        ArrayMap<String, Object> map = new ArrayMap<>();
//        map.put("key", key);
        return nbaApiT.getNBAJFTInfo(map);
    }

}
