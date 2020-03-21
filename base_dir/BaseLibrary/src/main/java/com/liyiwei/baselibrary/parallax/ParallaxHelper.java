package com.liyiwei.baselibrary.parallax;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.liyiwei.baselibrary.R;
import com.liyiwei.baselibrary.base.BaseApplication;
import com.liyiwei.baselibrary.parallax.widget.ParallaxBackLayout;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anzew on 2017-05-09.
 */
public class ParallaxHelper implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ParallaxHelper";
    private static ParallaxHelper sParallaxHelper;
    private LinkedStack<Activity, TraceInfo> mLinkedStack = new LinkedStack<>();
    private Set<String> mExclude = new HashSet<>();

    public static boolean isForeground = false;
    private long time = -2;
    private int count = 0;//0到后台，1,2当前
    private double lockPeriod = 30*1000d;//30s当v消息在后台运行超过30s，再启动程序时，需要手势密码解锁才能进入应用
    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ParallaxHelper getInstance() {
        if (sParallaxHelper == null)
            sParallaxHelper = new ParallaxHelper();
        return sParallaxHelper;
    }

    private ParallaxHelper() {

    }

    public void putExclude(String... activityNames) {
        mExclude.addAll(Arrays.asList(activityNames));
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        Log.e(TAG, "onActivityCreated():"+activity.getClass().getName());
        if (mExclude.contains(activity.getClass().getName())){
            return;
        }
//        if(activity instanceof AVChatActivity || activity instanceof TeamAVChatActivity){
//            return;
//        }
        
        final TraceInfo traceInfo = new TraceInfo();
        mLinkedStack.put(activity, traceInfo);
        traceInfo.mCurrent = activity;

        ParallaxBack parallaxBack = checkAnnotation(activity.getClass());
        if (mLinkedStack.size() > 0 && parallaxBack != null) {
            ParallaxBackLayout layout = enableParallaxBack(activity);
            layout.setEdgeFlag(parallaxBack.edge().getValue());
            layout.setEdgeMode(parallaxBack.edgeMode().getValue());
            layout.setLayoutType(parallaxBack.layout().getValue(),null);
        }
    }

    private ParallaxBack checkAnnotation(Class<? extends Activity> c) {
        Class mc = c;
        ParallaxBack parallaxBack;
        while (Activity.class.isAssignableFrom(mc)) {
            parallaxBack = (ParallaxBack) mc.getAnnotation(ParallaxBack.class);
            if (parallaxBack != null)
                return parallaxBack;
            mc = mc.getSuperclass();
        }
        return null;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e(TAG, "onActivityStarted():"+activity.getClass().getName());
        if (count == 0) {
//            Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
            long returnTime = System.currentTimeMillis();
            double sub = new BigDecimal(returnTime).subtract(new BigDecimal(time)).doubleValue();
            if (sub >= lockPeriod) {
                //此处是判断应用到后台多久时间以后需要开启手势密码
                //判断进入解锁手势密码
//                    Log.e(TAG, "in()：1");
//                startAuthActivity(activity);
            } else {
                time = System.currentTimeMillis();
            }
        }
//        else {
//            time = new Date().getTime();
//        }

        count++;
    }


    @Override
    public void onActivityResumed(Activity activity) {
        isForeground = true;
        Log.e(TAG, "onActivityResumed():"+activity.getClass().getName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "onActivityPaused():"+activity.getClass().getName());
//        activity.getWindow().getDecorView().buildDrawingCache();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e(TAG, "onActivityStopped():"+activity.getClass().getName());
        count--;
        if (count == 0) {
            time = System.currentTimeMillis();
            isForeground = false;
//            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
        }

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e(TAG, "onActivitySaveInstanceState():"+activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "onActivityDestroyed():"+activity.getClass().getName());
        mLinkedStack.remove(activity);
    }

    /**
     * Disable parallax back.
     *
     * @param activity the activity
     */
    public static void disableParallaxBack(Activity activity) {
        ParallaxBackLayout layout = getParallaxBackLayout(activity);
        if (layout != null)
            layout.setEnableGesture(false);
    }

    /**
     * Enable parallax back.
     *
     * @param activity the activity
     */
    public static ParallaxBackLayout enableParallaxBack(Activity activity) {
        ParallaxBackLayout layout = getParallaxBackLayout(activity, true);
        layout.setEnableGesture(true);
        return layout;
    }

    /**
     * Gets parallax back layout.
     *
     * @param activity the activity
     * @return the parallax back layout
     */
    public static ParallaxBackLayout getParallaxBackLayout(Activity activity) {
        return getParallaxBackLayout(activity, false);
    }

    /**
     * Gets parallax back layout.
     *
     * @param activity the activity
     * @param create   the create
     * @return the parallax back layout
     */
    public static ParallaxBackLayout getParallaxBackLayout(Activity activity, boolean create) {
        View view = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
        if (view instanceof ParallaxBackLayout)
            return (ParallaxBackLayout) view;
        view = activity.findViewById(R.id.pllayout);
        if (view instanceof ParallaxBackLayout)
            return (ParallaxBackLayout) view;
        if (create) {
            ParallaxBackLayout backLayout = new ParallaxBackLayout(activity);
            backLayout.setId(R.id.pllayout);
            backLayout.attachToActivity(activity);
            backLayout.setBackgroundView(new GoBackView(activity));
            return backLayout;
        }
        return null;
    }

    /**
     * The type Trace info.
     */
    public static class TraceInfo {
        private Activity mCurrent;
    }

    public static class GoBackView implements ParallaxBackLayout.IBackgroundView {

        private Activity mActivity;
        private Activity mActivityBack;

        private GoBackView(Activity activity) {
            mActivity = activity;
        }


        @Override
        public void draw(Canvas canvas) {
            if (mActivityBack != null) {
                mActivityBack.getWindow().getDecorView().requestLayout();
                mActivityBack.getWindow().getDecorView().draw(canvas);
            }
        }

        @Override
        public boolean canGoBack() {
            return (mActivityBack = sParallaxHelper.mLinkedStack.before(mActivity)) != null;
        }
    }

    public void exit(){
        try {
            for (int i = 0; i< mLinkedStack.size();i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mLinkedStack.getKey(i).finishAndRemoveTask();
                }else {
                    mLinkedStack.getKey(i).finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
