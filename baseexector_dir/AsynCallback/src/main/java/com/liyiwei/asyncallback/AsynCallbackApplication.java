package com.liyiwei.asyncallback;

import android.app.Application;
import android.util.Log;

public class AsynCallbackApplication extends Application {
    private static final String TAG = "AsynCallbackApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
    }
}
