package com.liyiwei.baselibrary.crash.appcrash;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.liyiwei.baselibrary.BuildConfig;
import com.liyiwei.baselibrary.R;
import com.liyiwei.baselibrary.crash.cockroach.Cockroach;
import com.liyiwei.baselibrary.crash.cockroach.ExceptionHandler;
import com.liyiwei.baselibrary.crash.support.CrashLog;
import com.liyiwei.baselibrary.crash.support.DebugSafeModeTipActivity;
import com.liyiwei.baselibrary.crash.support.DebugSafeModeUI;


public class BaseAppCrashApplication extends Application {
    private static final String TAG = "BaseAppCrashApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"作为module时的基础application");
        install();
    }

    private void install() {
        final Thread.UncaughtExceptionHandler sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler();
        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        DebugSafeModeUI.init(this);
        Cockroach.install(this, new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", throwable);
                CrashLog.saveCrashLog(getApplicationContext(), throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        toast.setText(R.string.safe_mode_excep_tips);
                        toast.show();
                    }
                });
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                throwable.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
                toast.setText("Cockroach Worked");
                toast.show();
            }

            @Override
            protected void onEnterSafeMode() {
                int tips = R.string.safe_mode_tips;
                Toast.makeText(BaseAppCrashApplication.this, getResources().getString(tips), Toast.LENGTH_LONG).show();
                DebugSafeModeUI.showSafeModeUI();

                if (BuildConfig.DEBUG) {
                    Intent intent = new Intent(BaseAppCrashApplication.this, DebugSafeModeTipActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                Thread thread = Looper.getMainLooper().getThread();
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }

        });

    }
}
