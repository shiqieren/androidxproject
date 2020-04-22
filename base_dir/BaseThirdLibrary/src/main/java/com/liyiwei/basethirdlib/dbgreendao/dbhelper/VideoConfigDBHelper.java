package com.liyiwei.basethirdlib.dbgreendao.dbhelper;

import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.VideoConfig;
import com.liyiwei.basethirdlib.dbgreendao.greendao.VideoConfigDao;

import java.util.ArrayList;
import java.util.List;

public class VideoConfigDBHelper {

    private static VideoConfigDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getVideoConfigDao();
    }

    public static List<VideoConfig> loadAll(long userId) {
        try {
            VideoConfigDao dao = getDao();
            if (dao == null) {
                return new ArrayList<>();
            } else {
                return dao.queryBuilder().where(VideoConfigDao.Properties.UserId.eq(userId)).list();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static VideoConfig getVideoConfig(long courseId, long userId, long fileId, double playProgress, int playSecond) {
        try {
            VideoConfig config = getDao().queryBuilder().where(VideoConfigDao.Properties.CourseId.eq(courseId)).where(VideoConfigDao.Properties.UserId.eq(userId)).unique();
            if (config == null) {
                config = new VideoConfig();
                config.setCourseId(courseId);
                config.setUserId(userId);
                config.setFileId(fileId);
                config.setPlaySecond(playSecond);
                config.setPlayProgress(playProgress);
                config.setFlag(false);
                config.setType(1);
                getDao().insert(config);
            } else {
                config.setCourseId(courseId);
                config.setUserId(userId);
                config.setFileId(fileId);
                config.setPlaySecond(playSecond);
                config.setPlayProgress(playProgress);
                config.setFlag(false);
            }
            return config;
        } catch (Exception e) {
            VideoConfig config = new VideoConfig();
            config.setCourseId(courseId);
            config.setUserId(userId);
            config.setFileId(fileId);
            config.setPlaySecond(playSecond);
            config.setPlayProgress(playProgress);
            config.setFlag(false);
            config.setType(1);
            try {
                getDao().insert(config);
            }catch (Exception e1){
                getDao().insertOrReplace(config);
            }
            return config;
        }
    }

    public static VideoConfig getVideoConfig(long courseId, long userId, long fileId) {
        try {
            VideoConfig config = getDao().queryBuilder().where(VideoConfigDao.Properties.CourseId.eq(courseId)).where(VideoConfigDao.Properties.UserId.eq(userId)).unique();
            if (config == null) {
                config = new VideoConfig();
                config.setCourseId(courseId);
                config.setUserId(userId);
                config.setFileId(fileId);
                config.setPlaySecond(0);
                config.setPlayProgress(0);
                config.setFlag(false);
                config.setType(2);
                getDao().insert(config);
            } else {
                config.setCourseId(courseId);
                config.setUserId(userId);
                config.setFileId(fileId);
                config.setPlaySecond(0);
                config.setPlayProgress(0);
                config.setFlag(false);
                config.setType(2);
            }
            return config;
        } catch (Exception e) {
            VideoConfig config = new VideoConfig();
            config.setCourseId(courseId);
            config.setUserId(userId);
            config.setFileId(fileId);
            config.setPlaySecond(0);
            config.setPlayProgress(0);
            config.setFlag(false);
            config.setType(2);
            return config;
        }
    }


    public static void insertOrUpdate(VideoConfig config) {
        try {
            getDao().insertOrReplace(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteConfig(long id) {
        try {
            getDao().deleteByKey(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
