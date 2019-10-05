package com.liyiwei.baseroute;

import android.app.Application;
import android.util.Log;

public class BaseRouteApplication extends Application {
    private static final String TAG = "BaseNetWorkApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
    }
}
