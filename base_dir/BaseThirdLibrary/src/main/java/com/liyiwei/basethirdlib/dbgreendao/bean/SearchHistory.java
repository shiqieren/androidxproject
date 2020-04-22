package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SearchHistory {

    private long time;
    @Id
    private String key;
    @Generated(hash = 35213036)
    public SearchHistory(long time, String key) {
        this.time = time;
        this.key = key;
    }
    @Generated(hash = 1905904755)
    public SearchHistory() {
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
