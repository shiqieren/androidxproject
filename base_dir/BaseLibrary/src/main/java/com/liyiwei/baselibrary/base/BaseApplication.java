package com.liyiwei.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.liyiwei.baselibrary.BuildConfig;
import com.liyiwei.baselibrary.crash.appcrash.BaseAppCrashApplication;
import com.liyiwei.baselibrary.parallax.ParallaxHelper;
import com.liyiwei.baselibrary.service.NetworkService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.LinkedList;
import java.util.List;

/**
 * @创建者 CSDN_LQR
 * @描述 Application基类
 */
public class BaseApplication extends BaseAppCrashApplication {
    private static BaseApplication application = null;

    public static BaseApplication getApplication(){
        return application;
    }
    public static BaseApplication getInstance(){
        return application;
    }
    public static List<Activity> activities = new LinkedList<>();

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler

    private ParallaxHelper iActivityLifecycleCallbacks;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

        startDebugStrictMode();
        application = this;
        //对全局属性赋值
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
        registerLifecycle();

        // 网络监听服务
        NetworkService.enqueueWork(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        BaseApplication.mContext = mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static void setMainThread(Thread mMainThread) {
        BaseApplication.mMainThread = mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mMainThreadId) {
        BaseApplication.mMainThreadId = mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainThreadLooper(Looper mMainLooper) {
        BaseApplication.mMainLooper = mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler mHandler) {
        BaseApplication.mHandler = mHandler;
    }

    public void registerLifecycle() {
        if (iActivityLifecycleCallbacks == null) {
            iActivityLifecycleCallbacks = ParallaxHelper.getInstance();
            registerActivityLifecycleCallbacks(iActivityLifecycleCallbacks);
        }
    }

    public void unRegisterLifecycle() {
        if (iActivityLifecycleCallbacks != null) {
            unregisterActivityLifecycleCallbacks(ParallaxHelper.getInstance());
        }
    }

    /**
     * 严苛模式
     * 仅在Debug模式启用StrictMode
     */
    private void startDebugStrictMode() {
        if (BuildConfig.DEBUG) {
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
//                  .detectResourceMismatches()
                    .detectCustomSlowCalls()
                    .penaltyLog()
//					.penaltyDialog()
                    .build());
//			}

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .build());
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        return BaseApplication.getInstance().refWatcher;
    }

    /**
     * 重启当前应用
     */
    public static void restart() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 完全退出
     * 一般用于“退出程序”功能
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 退出应用程序
     */
    public static void AppExit() {
        ParallaxHelper.getInstance().exit();
//		try {
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(0);
//		} catch (Exception e) {
//		}
    }
}
