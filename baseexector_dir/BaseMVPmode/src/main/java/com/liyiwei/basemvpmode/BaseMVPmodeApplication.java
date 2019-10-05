package com.liyiwei.basemvpmode;

import android.app.Application;
import android.util.Log;

public class BaseMVPmodeApplication extends Application {
    private static final String TAG = "BaseMVPmodeApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
    }
}
