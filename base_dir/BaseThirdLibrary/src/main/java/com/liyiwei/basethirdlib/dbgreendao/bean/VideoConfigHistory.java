package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

//用来记录上传的videoConfig的
@Entity
public class VideoConfigHistory {
    @Id
    private String id;//由courseID，startTime，endTime拼接而成

    @Generated(hash = 435651886)
    public VideoConfigHistory(String id) {
        this.id = id;
    }

    @Generated(hash = 1980393272)
    public VideoConfigHistory() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
