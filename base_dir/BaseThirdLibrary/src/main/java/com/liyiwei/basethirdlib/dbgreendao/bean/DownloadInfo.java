package com.liyiwei.basethirdlib.dbgreendao.bean;


import com.liyiwei.basethirdlib.dbgreendao.greendao.DaoSession;
import com.liyiwei.basethirdlib.dbgreendao.greendao.DownloadInfoDao;
import com.liyiwei.basethirdlib.dbgreendao.greendao.SubDownloadInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity
public class DownloadInfo {

    /**
     * 下载状态
     */
    public static final int READY = 0; //准备下载
    public static final int LOADING = 1; //下载中
    public static final int PAUSE = 2; //暂停
    public static final int CANCEL = 3; //被取消
    public static final int OVER = 4;  //下载完成
    public static final int ERROR = 5; //下载错误


    public static final long TOTAL_ERROR = -1;//获取进度失败
    @Id
    private String md5;
    private String url;
    private String fileName;
    private String filePath;
    private int state;
    private long total;
    private long progress;
    private long currSize;
    @ToMany(referencedJoinProperty = "pid")
    List<SubDownloadInfo> infos;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1465593784)
    private transient DownloadInfoDao myDao;

    @Generated(hash = 1864322849)
    public DownloadInfo(String md5, String url, String fileName, String filePath, int state,
                        long total, long progress, long currSize) {
        this.md5 = md5;
        this.url = url;
        this.fileName = fileName;
        this.filePath = filePath;
        this.state = state;
        this.total = total;
        this.progress = progress;
        this.currSize = currSize;
    }

    @Generated(hash = 327086747)
    public DownloadInfo() {
    }

    public DownloadInfo(String url) {
        this.url = url;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return this.progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getCurrSize() {
        return this.currSize;
    }

    public void setCurrSize(long currSize) {
        this.currSize = currSize;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 785866771)
    public List<SubDownloadInfo> getInfos() {
        if (infos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubDownloadInfoDao targetDao = daoSession.getSubDownloadInfoDao();
            List<SubDownloadInfo> infosNew = targetDao
                    ._queryDownloadInfo_Infos(md5);
            synchronized (this) {
                if (infos == null) {
                    infos = infosNew;
                }
            }
        }
        return infos;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 894428592)
    public synchronized void resetInfos() {
        infos = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 17038220)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDownloadInfoDao() : null;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}