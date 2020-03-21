package com.liyiwei.baselibrary.statusbar.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.liyiwei.baselibrary.R;
import com.liyiwei.baselibrary.util.apputil.AppManager;
import com.liyiwei.baselibrary.util.xutils.Utils;

import org.greenrobot.greendao.annotation.NotNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * DialogFragment 实现沉浸式的基类
 *
 * @author geyifeng
 * @date 2017 /8/26
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected Activity mActivity;
    protected View mRootView;
    protected Window mWindow;
    private Unbinder unbinder;
    public Integer[] mWidthAndHeight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);
        mWindow = dialog.getWindow();
//        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initData();
        initView();
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppManager.getInstance().hideSoftKeyBoard(mActivity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        ImmersionBar.with(this).init();
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }
}
//////////////////////////////full///////////////
//@Override
//public void onStart() {
//    super.onStart();
//    mWindow.setWindowAnimations(R.style.RightAnimation);
//}
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.dialog;
//    }
//
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this)
//                .titleBar(toolbar)
//                .statusBarDarkFont(true)
//                .navigationBarColor(R.color.btn3)
//                .keyboardEnable(true)
//                .init();
//    }
////////////////////////////bottom////////////////////////
//@Override
//public void onStart() {
//    super.onStart();
//    mWindow.setGravity(Gravity.BOTTOM);
//    mWindow.setWindowAnimations(R.style.BottomAnimation);
//    mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
//}
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.dialog;
//    }
//
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this).navigationBarColor(R.color.cool_green_normal).init();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
//    }
//////////////////////////left//////////////
//@Override
//public void onStart() {
//    super.onStart();
//    mWindow.setGravity(Gravity.TOP | Gravity.START);
//    mWindow.setWindowAnimations(R.style.LeftAnimation);
//    mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
//}
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.dialog;
//    }
//
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this).titleBar(toolbar)
//                .navigationBarColor(R.color.btn11)
//                .keyboardEnable(true)
//                .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//                .init();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
//        ImmersionBar.with(this)
//                .navigationBarWithKitkatEnable(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//                .init();
//    }
/////////////////////////////right/////////////////////
//@Override
//public void onStart() {
//    super.onStart();
//    mWindow.setGravity(Gravity.TOP | Gravity.END);
//    mWindow.setWindowAnimations(R.style.RightAnimation);
//    mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
//}
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.dialog;
//    }
//
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this).titleBar(toolbar)
//                .navigationBarColor(R.color.btn8)
//                .keyboardEnable(true)
//                .init();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
//    }
/////////////////////////top/////////////////////////////////
//@Override
//public void onStart() {
//    super.onStart();
//    mWindow.setGravity(Gravity.TOP);
//    mWindow.setWindowAnimations(R.style.TopAnimation);
//    mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
//}
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.dialog;
//    }
//
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this)
//                .titleBar(toolbar)
//                .navigationBarColor(R.color.btn4)
//                .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                .init();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
//        ImmersionBar.with(this)
//                .navigationBarWithKitkatEnable(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//                .init();
//    }
