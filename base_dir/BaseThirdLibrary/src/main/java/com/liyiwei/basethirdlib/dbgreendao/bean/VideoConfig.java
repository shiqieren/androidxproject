package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class VideoConfig {

    @Id
    private long courseId;
    private long userId;
    private long fileId;
    private double playProgress;//音视频播放进度
    private int playSecond; //音视频已播放秒数
    private boolean flag; // 是否上传成功
    private long startTime;
    private long endTime;
    private int type;  //类型，1为音视频，2为文档


    @Generated(hash = 1508857869)
    public VideoConfig(long courseId, long userId, long fileId, double playProgress,
            int playSecond, boolean flag, long startTime, long endTime, int type) {
        this.courseId = courseId;
        this.userId = userId;
        this.fileId = fileId;
        this.playProgress = playProgress;
        this.playSecond = playSecond;
        this.flag = flag;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    @Generated(hash = 2089294651)
    public VideoConfig() {
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public double getPlayProgress() {
        return playProgress;
    }

    public void setPlayProgress(double playProgress) {
        this.playProgress = playProgress;
    }

    public int getPlaySecond() {
        return playSecond;
    }

    public void setPlaySecond(int playSecond) {
        this.playSecond = playSecond;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
