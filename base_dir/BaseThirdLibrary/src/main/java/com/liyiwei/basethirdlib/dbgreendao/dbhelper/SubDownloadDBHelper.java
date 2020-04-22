package com.liyiwei.basethirdlib.dbgreendao.dbhelper;


import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.SubDownloadInfo;
import com.liyiwei.basethirdlib.dbgreendao.greendao.SubDownloadInfoDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubDownloadDBHelper {
    private static SubDownloadInfoDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getSubDownloadInfoDao();
    }

    public static void insertOrReplace(List<SubDownloadInfo> infos) {
        getDao().insertOrReplaceInTx(infos);
    }

    public static void update(SubDownloadInfo info) {
        getDao().update(info);
    }

    public static boolean isAllReady(String pid) {
        return getDao().queryBuilder().where(SubDownloadInfoDao.Properties.Pid.eq(pid)).where(SubDownloadInfoDao.Properties.Status.eq(0))
                .count() == 0;
    }

    public static List<File> getAllFileByPid(String pid) {
        List<SubDownloadInfo> infos = getDao().queryBuilder().where(SubDownloadInfoDao.Properties.Pid.eq(pid)).where(SubDownloadInfoDao.Properties.Status.eq(1))
                .orderAsc(SubDownloadInfoDao.Properties.Start).list();
        List<File> files = new ArrayList<>();
        for (SubDownloadInfo info : infos) {
            files.add(new File(info.getPath()));
        }
        return files;
    }

}
