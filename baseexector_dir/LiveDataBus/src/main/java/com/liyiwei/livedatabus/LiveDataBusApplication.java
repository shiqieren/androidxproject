package com.liyiwei.livedatabus;

import android.app.Application;
import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;

public class LiveDataBusApplication extends Application {
    private static final String TAG = "LiveDataBusApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
        LiveEventBus
                .config()
                .supportBroadcast(this)
                .lifecycleObserverAlwaysActive(true);
    }
}
