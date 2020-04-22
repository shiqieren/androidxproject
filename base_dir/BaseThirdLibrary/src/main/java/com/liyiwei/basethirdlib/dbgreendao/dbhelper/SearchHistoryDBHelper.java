package com.liyiwei.basethirdlib.dbgreendao.dbhelper;

import android.text.TextUtils;

import com.liyiwei.basethirdlib.BaseThirdApplication;
import com.liyiwei.basethirdlib.dbgreendao.bean.SearchHistory;
import com.liyiwei.basethirdlib.dbgreendao.greendao.SearchHistoryDao;

import java.util.List;


public class SearchHistoryDBHelper {
    private static SearchHistoryDao getDao() {
        return BaseThirdApplication.getInstance().getDaoSession().getSearchHistoryDao();
    }

    public static void inserOrReplace(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SearchHistory history = new SearchHistory();
        history.setKey(key);
        history.setTime(System.currentTimeMillis());

        getDao().insertOrReplace(history);
    }

    public static List<SearchHistory> getHistorys(int limit) {
        return getDao().queryBuilder().limit(limit).orderDesc(SearchHistoryDao.Properties.Time).list();
    }
}
