package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SubDownloadInfo {
    @Id(autoincrement = true)
    private Long id;

    private String pid;

    private long start;
    private long end;

    private int index;
    private String path;
    private String url;
    private int status;  //0未下载，1已下载

    @Generated(hash = 1569412190)
    public SubDownloadInfo(Long id, String pid, long start, long end, int index,
                           String path, String url, int status) {
        this.id = id;
        this.pid = pid;
        this.start = start;
        this.end = end;
        this.index = index;
        this.path = path;
        this.url = url;
        this.status = status;
    }

    @Generated(hash = 1140635240)
    public SubDownloadInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
