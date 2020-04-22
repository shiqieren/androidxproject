package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ExamCache {
    @Id
    private long paperId;
    private int currentCount;
    private long takeMil;
    private String questions;





    @Generated(hash = 1594775956)
    public ExamCache(long paperId, int currentCount, long takeMil,
                     String questions) {
        this.paperId = paperId;
        this.currentCount = currentCount;
        this.takeMil = takeMil;
        this.questions = questions;
    }

    @Generated(hash = 373633188)
    public ExamCache() {
    }





    public long getPaperId() {
        return this.paperId;
    }

    public void setPaperId(long paperId) {
        this.paperId = paperId;
    }

    public int getCurrentCount() {
        return this.currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public long getTakeMil() {
        return this.takeMil;
    }

    public void setTakeMil(long takeMil) {
        this.takeMil = takeMil;
    }

    public String getQuestions() {
        return this.questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}
