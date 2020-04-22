package com.liyiwei.basethirdlib.dbgreendao.dbhelper;


import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.ExamCache;
import com.liyiwei.basethirdlib.dbgreendao.greendao.ExamCacheDao;

public class ExamCacheDBHelper {

    private static ExamCacheDao getDao() {
        BaseThirdApplication.getInstance().getDaoSession().getExamCacheDao().detachAll();
        return BaseThirdApplication.getInstance().getDaoSession().getExamCacheDao();
    }

    public static void delete(ExamCache cache) {
        getDao().delete(cache);
    }

    public static void deleateAll(){
        getDao().deleteAll();
    }

    public static ExamCache getExamCache(long examId) {
        BaseThirdApplication.getInstance().getDaoSession().clear();
        ExamCache cache = getDao().load(examId);
        if (cache == null) {
            cache = new ExamCache();
            cache.setPaperId(examId);
            getDao().insert(cache);
            return getDao().load(examId);
        }
        return cache;
    }

    public static void update(ExamCache cache) {
        getDao().update(cache);
    }
}
