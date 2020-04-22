package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ExamPaperConfig {
    @Id
    private Integer paperId;
    private Integer lineId;
    @Generated(hash = 308647018)
    public ExamPaperConfig(Integer paperId, Integer lineId) {
        this.paperId = paperId;
        this.lineId = lineId;
    }
    @Generated(hash = 1416550280)
    public ExamPaperConfig() {
    }
    public Integer getPaperId() {
        return this.paperId;
    }
    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }
    public Integer getLineId() {
        return this.lineId;
    }
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

}
