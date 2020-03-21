package com.liyiwei.baselibrary.statusbar.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.liyiwei.baselibrary.R;

import butterknife.ButterKnife;

/**
 * Activity基类
 *
 * @author geyifeng
 * @date 2017/5/9
 */
public abstract class BaseActivity extends com.liyiwei.baselibrary.base.BaseActivity {

    protected String mTag = this.getClass().getSimpleName();

    protected Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        //绑定控件
//        ButterKnife.bind(this);
        //初始化沉浸式
        initImmersionBar();
        //初始化数据
        initData();
        //view与数据绑定
        initView();
        //设置监听
        setListener();
    }


    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init();
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setListener() {
    }
}
