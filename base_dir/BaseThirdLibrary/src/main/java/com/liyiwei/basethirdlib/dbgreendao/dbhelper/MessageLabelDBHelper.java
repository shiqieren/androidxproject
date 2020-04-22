package com.liyiwei.basethirdlib.dbgreendao.dbhelper;

import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.MessageLabel;
import com.liyiwei.basethirdlib.dbgreendao.greendao.MessageLabelDao;

import java.util.ArrayList;
import java.util.List;

public class MessageLabelDBHelper {
    private static MessageLabelDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getMessageLabelDao();
    }

    public static boolean hasCreateMessageLabel(List<MessageLabel> list){
        boolean hasCreate=false;
        for(MessageLabel messageLabel:list){
            MessageLabel label=getDao().load(messageLabel.getLabel());
            if(label==null){
                hasCreate=true;
                break;
            }
        }
        return hasCreate;
    }

    public static void saveOrUpdate(List<MessageLabel> list) {
        getDao().insertOrReplaceInTx(list);
    }

//    public static Flowable<List<MessageLabel>> getAll() {
//        List<MessageLabel> v=new ArrayList<>();
//        MessageLabel messageLabel = new MessageLabel();
//        messageLabel.setName(LearningApp.getInstance().getString(R.string.all));
//        messageLabel.setLabel("");
//        v.add(0, messageLabel);
//        return Flowable.just(v);
//    }

}
