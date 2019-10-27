package com.sie.mp.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sie.mp.app.BroadcastConstants;
import com.sie.mp.app.IMApplication;
import com.sie.mp.app.URLConstants;
import com.sie.mp.data.EntityUtils;
import com.sie.mp.data.MpConversationReadedItem;
import com.sie.mp.msg.utils.ChatHisUtils;
import com.sie.mp.space.utils.VLog;
import com.sie.mp.vivo.GlobalVars;
import com.sie.mp.vivo.ResourceBook;
import com.sie.mp.vivo.dao.MpChatHisDao;
import com.sie.mp.vivo.exception.SNSException;
import com.sie.mp.vivo.http.HttpParameter;
import com.sie.mp.vivo.mblog.MicroBlog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author
 * @version 1.0
 * @date 2017/6/26
 */

public class RefreshReadedConversation {
    private Context context;
    private MicroBlog microBlog;
    private String mResultMsg;
    private long userId;
    private ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();

    public RefreshReadedConversation(Context context) {
        this.context = context;
        try {
            userId = IMApplication.getInstance().getCurrentUser().getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MyAsyncTask().executeOnExecutor(FULL_TASK_EXECUTOR);
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            if (userId == 0) {
                return 0;
            }
            microBlog = GlobalVars.getMicroBlog();
            long last_max_updatetimemills = EntityUtils.getMaxUpdateTimemills();
//            long last_max_updatetimemills = MpChatHisDao.getInstance().getMaxUpdateTimemills();
//            last_max_updatetimemills = 1563281773037L;
            VLog.v("test_ccb", "last_max_updatetimemills:" + last_max_updatetimemills);
            if (last_max_updatetimemills < 0) {
                return 0;
            }
            List<HttpParameter> params = new ArrayList<HttpParameter>();
            params.add(new HttpParameter("userId", userId));
            params.add(new HttpParameter("clientType", "MOBILE"));
            params.add(new HttpParameter("updateTimeMills", last_max_updatetimemills));
            try {
                String data = microBlog.publicPost(URLConstants.MP_HOST + "com.sie.mp.chatmsg.conversation.queryReadedConversation.biz.ext", params);
                JSONObject jsonObject = new JSONObject(data);
                VLog.e("retrofit", "queryReadedConversation:" + data);
                String conversation = jsonObject.getString("readedConversation");
                if (conversation != null) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    List<MpConversationReadedItem> readedConversation = ((List<MpConversationReadedItem>) gson.fromJson(conversation, new TypeToken<List<MpConversationReadedItem>>() {
                    }.getType()));
                    long updatetimemills = 0L;
                    if (readedConversation != null && readedConversation.size() > 0) {
                        for (int i = 0; i < readedConversation.size(); i++) {
                            MpConversationReadedItem item = readedConversation.get(i);
                            updatetimemills = Math.max(updatetimemills, item.getUpdatetimemills());
                            VLog.e("test_ccb", "updatetimemills:" + updatetimemills);
                            ChatHisUtils.resetConversationReaded(item);
                        }

                        EntityUtils.setMaxUpdateTimemills(updatetimemills);
                        return 1;
                    }
                }

            } catch (JsonSyntaxException e) {
            } catch (SNSException snsException) {
                mResultMsg = snsException.getExceptionCode() == 1103 ? null : ResourceBook.getStatusCodeValue(snsException.getExceptionCode(), context);
                snsException.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer > 0) {
                Intent bc = new Intent(BroadcastConstants.BROADCAST_MAIN_METHOD_LOAD_CHAT_NUM);
                IMApplication.getInstance().sendBroadcast(bc);
            }
            if (mResultMsg != null) {
                T.showShort(context, mResultMsg);
            }
        }
    }


}
