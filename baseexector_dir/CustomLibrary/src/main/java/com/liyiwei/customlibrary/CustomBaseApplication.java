package com.liyiwei.customlibrary;

import android.app.Application;
import android.util.Log;

public class CustomBaseApplication extends Application {
    private static final String TAG = "CustomBaseApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
    }
}
