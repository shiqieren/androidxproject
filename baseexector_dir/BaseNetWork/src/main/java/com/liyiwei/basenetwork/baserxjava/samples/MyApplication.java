package com.liyiwei.basenetwork.baserxjava.samples;

import android.app.Application;


import com.liyiwei.basenetwork.baserxjava.samples.model.Events;
import com.liyiwei.basenetwork.baserxjava.samples.ui.rxbus.RxBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by threshold on 2017/1/12.
 */

public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    private RxBus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new RxBus();
    }

    public RxBus bus() {
        return bus;
    }

    public void sendAutoEvent() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        bus.send(new Events.AutoEvent());
                    }
                });
    }

}
