package com.liyiwei.basethirdlib.dbgreendao.dbhelper;


import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.DownloadInfo;
import com.liyiwei.basethirdlib.dbgreendao.greendao.DownloadInfoDao;

import java.util.List;

public class DownloadInfoDBHelper {
    private static DownloadInfoDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getDownloadInfoDao();
    }

//    public static List<DownloadInfo> getAllAsFlowable() {
//        return getDao().loadAll();
//    }

    public static void insertOrReplace(DownloadInfo info) {
        getDao().insertOrReplace(info);
    }

    public static boolean isExit(String url) {
        return getDao().load(url) != null;
    }

//    public static Flowable<DownloadInfo> getAllAsFlowable() {
//        return Flowable.fromIterable(getDao().loadAll()).subscribeOn(Schedulers.io());
//    }
//
//    public static Flowable<DownloadInfo> getAsFlowable(String id) {
//        return Flowable.just(getDao().load(id)).subscribeOn(Schedulers.io());
//    }

    public static List<DownloadInfo> getAll() {
        return getDao().loadAll();
    }

    public static DownloadInfo getDownloadInfo(String id) {
        return getDao().load(id);
    }

    public static void update(DownloadInfo info) {
        getDao().update(info);
    }
}
