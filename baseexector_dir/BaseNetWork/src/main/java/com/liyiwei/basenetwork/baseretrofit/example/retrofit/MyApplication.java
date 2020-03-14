package com.liyiwei.basenetwork.baseretrofit.example.retrofit;

import android.app.Application;
import android.content.Context;

import com.liyiwei.baselibrary.BuildConfig;
import com.liyiwei.basenetwork.baseretrofit.RxRetrofitApp;

/**
 * Created by WZG on 2016/10/25.
 */

public class MyApplication extends Application{
    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=getApplicationContext();
        RxRetrofitApp.init(this, BuildConfig.DEBUG);
    }
}
