package com.liyiwei.basethirdlib;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liyiwei.basethirdlib.dbgreendao.greendao.DaoMaster;
import com.liyiwei.basethirdlib.dbgreendao.greendao.DaoSession;

public class BaseThirdApplication extends Application {
    private static final String TAG = "BaseNetWorkApplication";
    private static BaseThirdApplication sInstance;
    private DaoMaster.DevOpenHelper helper;//获取Helper对象
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    public DaoSession getDaoSession() {
        return daoSession;
    }
    public static BaseThirdApplication getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
        new Thread(new Runnable() {
            @Override
            public void run() {
                setupApp();
            }
        }).start();
    }

    private void setupApp() {
        helper = new DaoMaster.DevOpenHelper(this, "learning-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
