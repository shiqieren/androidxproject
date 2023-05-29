package com.liyiwei.basethirdlib;

import android.app.Application;
import android.util.Log;


public class BaseThirdApplication extends Application {
    private static final String TAG = "BaseNetWorkApplication";
    private static BaseThirdApplication sInstance;

    public static BaseThirdApplication getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
    }
}
