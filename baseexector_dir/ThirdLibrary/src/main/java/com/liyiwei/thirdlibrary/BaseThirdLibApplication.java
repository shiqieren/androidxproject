package com.liyiwei.thirdlibrary;

import android.app.Application;
import android.util.Log;

public class BaseThirdLibApplication extends Application {
    private static final String TAG = "CustomBaseApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
    }
}
