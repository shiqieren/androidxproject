package com.liyiwei.basethirdlib.dbgreendao.dbhelper;

import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.VideoConfig;
import com.liyiwei.basethirdlib.dbgreendao.bean.VideoConfigHistory;
import com.liyiwei.basethirdlib.dbgreendao.greendao.VideoConfigHistoryDao;

public class VideoConfigHistoryDBHelper {
    private static VideoConfigHistoryDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getVideoConfigHistoryDao();
    }

    public static boolean isNeedUpload(VideoConfig config) {
        if (config == null) {
            return false;
        }
        if (config.getStartTime() > config.getEndTime()) {
            return false;
        }
        String id = config.getStartTime() + "_" + config.getCourseId();
        return getDao().load(id) == null;
    }

    public static void insertOrUpdate(VideoConfig config) {
        String id = config.getStartTime() + "_" + config.getCourseId();
        getDao().insertOrReplace(new VideoConfigHistory(id));
    }

    public static void deleteConfig(VideoConfig config) {
        String id = config.getStartTime() + "_" + config.getCourseId();
        getDao().deleteByKey(id);
    }
}
