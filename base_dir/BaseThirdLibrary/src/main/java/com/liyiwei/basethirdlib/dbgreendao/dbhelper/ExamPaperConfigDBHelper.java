package com.liyiwei.basethirdlib.dbgreendao.dbhelper;


import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.ExamPaperConfig;
import com.liyiwei.basethirdlib.dbgreendao.greendao.ExamPaperConfigDao;

public class ExamPaperConfigDBHelper {
    private static ExamPaperConfigDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getExamPaperConfigDao();
    }

    public static ExamPaperConfig load(int id){
        return getDao().load(id);
    }
}
