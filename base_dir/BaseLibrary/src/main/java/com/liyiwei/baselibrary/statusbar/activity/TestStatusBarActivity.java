package com.liyiwei.baselibrary.statusbar.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.BarProperties;
import com.gyf.immersionbar.ImmersionBar;
import com.liyiwei.baselibrary.R;
import com.liyiwei.baselibrary.event.NetworkEvent;
import com.liyiwei.baselibrary.statusbar.bean.FunBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TestStatusBarActivity extends BaseActivity{
    private ArrayList<FunBean> mMainData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).keyboardEnable(true).init();//方式1：初始化状态栏
        //ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();//方式2：初始化自定义toolbar
        //ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#F56A3D").keyboardEnable(true).init();//方式3：带色值初始化
        //ImmersionBar.with(this).fitsSystemWindows(true).statusBarColorInt(Color.BLACK).navigationBarColorInt(Color.BLACK).autoDarkModeEnable(true).init();//方式4：可变色自定义颜色初始化+用法2
        //ImmersionBar.with(this).fitsSystemWindows(true).statusBarColorInt(Color.WHITE).statusBarDarkFont(true, 0.2f).init();//方式5：
//        ImmersionBar.with(this).titleBar(R.id.toolbar).setOnBarListener(this::adjustView).init();//方式6适配刘海屏
        //ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary)//方法7距离高度
        //                .titleBarMarginTop(R.id.toolbar)
        //                .statusBarColor(R.color.colorPrimary)
        //                .keyboardEnable(true)
        //                .init();
        //
        //        //或者使用静态方法设置
        //        //ImmersionBar.setTitleBarMarginTop(this,toolbar);
//        ImmersionBar.with(this).titleBar(mToolbar)//方式8：导航栏
//                .setOnNavigationBarListener(show -> {
//                    initView();
//                    Toast.makeText(this, "导航栏" + (show ? "显示了" : "隐藏了"), Toast.LENGTH_SHORT).show();
//                })
//                .navigationBarColor(R.color.btn13).init();
    }

    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();
//        mMainData = DataUtils.getMainData(this);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        addNetworkView();
        //////////////用法1：使用actionbar/////////////
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("结合actionBar使用");
        }
        ImmersionBar.with(this).supportActionBar(true).statusBarColor(R.color.colorPrimary).init();
        ///////////////////////用法2：改变颜色+初始方式4//////////////////////
        //        ImmersionBar.with(this).statusBarColorInt(色值).navigationBarColorInt(色值).init();
        ///////////////////////用法3:透明度调整+初始方式4//////////////////////
        //        ImmersionBar.with(ColorActivity.this).barAlpha(alpha)
        //                .statusBarColorTransform(R.color.btn14)
        //                .navigationBarColorTransform(R.color.btn3)
        //                .addViewSupportTransformColor(mToolbar)
        //                .addViewSupportTransformColor(btn1, R.color.btn1, R.color.btn4)
        //                .addViewSupportTransformColor(btn2, R.color.btn3, R.color.btn12)
        //                .addViewSupportTransformColor(btn3, R.color.btn5, R.color.btn10)
        //                .addViewSupportTransformColor(linearLayout, R.color.darker_gray, R.color.btn5)
        //                .init();
        ///////////////////////////////////////////////////////////////////////////////
        BarParams barParams = ImmersionBar.with(this).getBarParams();
        if (barParams.fits) {
            ImmersionBar.with(this).fitsSystemWindows(false).transparentStatusBar().init();
//            button.setText("fitsSystemWindows动态演示:false");
        }else {
            ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init();
//            button.setText("fitsSystemWindows动态演示:true");
        }
    }

    private void addNetworkView() {
//        mNetworkView = LayoutInflater.from(this).inflate(R.layout.item_network, mRv, false);
//        if (!Utils.isNetworkConnected(this)) {
//            mMainAdapter.addHeaderView(mNetworkView);
//        }
    }

    /**
     * 适配刘海屏遮挡数据问题
     * Adjust view.
     *
     * @param barProperties the bar properties,ImmersionBar#setOnBarListener
     */
    private void adjustView(BarProperties barProperties) {
//        if (barProperties.isNotchScreen()) {
//            if (mMainData != null) {
//                for (FunBean funBean : mMainData) {
//                    if (barProperties.isPortrait()) {
//                        funBean.setMarginStart(DensityUtil.dip2px(this, 8));
//                        funBean.setMarginEnd(DensityUtil.dip2px(this, 8));
//                    } else {
//                        if (barProperties.isLandscapeLeft()) {
//                            funBean.setMarginStart(DensityUtil.dip2px(this, 8) + barProperties.getNotchHeight());
//                            funBean.setMarginEnd(DensityUtil.dip2px(this, 8));
//                        } else {
//                            funBean.setMarginStart(DensityUtil.dip2px(this, 8));
//                            funBean.setMarginEnd(DensityUtil.dip2px(this, 8) + barProperties.getNotchHeight());
//                        }
//                    }
//                }
//            }
//            if (mMainAdapter != null) {
//                mMainAdapter.notifyDataSetChanged();
//            }
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent networkEvent) {
//        if (mNetworkView != null) {
//            if (networkEvent.isAvailable()) {
//                if (mNetworkView.getParent() != null) {
//                    mMainAdapter.removeHeaderView(mNetworkView);
//                }
//                if (mBannerAdapter != null && mBannerPosition != -1) {
//                    mBannerAdapter.notifyDataSetChanged();
//                    ArrayList<String> data = mBannerAdapter.getData();
//                    String s = data.get(mBannerPosition % data.size());
//                    GlideUtils.loadBlurry(mIvBanner, s);
//                }
//            } else {
//                if (mNetworkView.getParent() == null) {
//                    mMainAdapter.addHeaderView(mNetworkView);
//                }
//            }
//        }
    }
}

////////////////////////////解决软键盘与底部输入框冲突问题 start////////////////////////
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this).titleBar(toolbar)
//                //解决软键盘与底部输入框冲突问题
//                .keyboardEnable(true)
//                .init();
//    }
//    @Override
//    protected void initView() {
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        listView.setAdapter(new SimpleAdapter(this, mapList, R.layout.item_simple, new String[]{"desc"}, new int[]{R.id.text}));
//    }
//
//    @Override
//    protected void setListener() {
//        //toolbar返回按钮监听
//        toolbar.setNavigationOnClickListener(v -> finish());
//    }
////////////////////////////解决软键盘与底部输入框冲突问题 end///////////////////////
//      隐藏导航
//      if (ImmersionBar.hasNavigationBar(this)) {
//        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
//       }else"当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统"
// 全屏 if (ImmersionBar.hasNavigationBar(this)) {
//                        BarParams barParams = ImmersionBar.with(this).getBarParams();
//                        if (barParams.fullScreen) {
//                            ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).init();
//                        } else {
//                            ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().init();
//                        }
//                    }else"当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统"
//字体变色
//if (ImmersionBar.isSupportStatusBarDarkFont()) {
//        ImmersionBar.with(this).statusBarDarkFont(true).init();
//        }else "当前设备不支持状态栏字体变色"
///////////////////////////////////////头部塞入可滑动列表////////////////////////////////
//private void addBannerView() {
//    View bannerView = LayoutInflater.from(this).inflate(R.layout.item_main_banner, mRv, false);
//    mIvBanner = bannerView.findViewById(R.id.iv_banner);
//    RecyclerView recyclerView = bannerView.findViewById(R.id.rv_content);
//    ViewUtils.increaseViewHeightByStatusBarHeight(this, mIvBanner);
//    ImmersionBar.setTitleBarMarginTop(this, recyclerView);
//
//    mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//    recyclerView.setLayoutManager(mLayoutManager);
//    PagerSnapHelper snapHelper = new PagerSnapHelper();
//    snapHelper.attachToRecyclerView(recyclerView);
//    mBannerAdapter = new BannerAdapter(Utils.getPics());
//    recyclerView.setAdapter(mBannerAdapter);
//
//    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            if (mBannerPosition != mLayoutManager.findFirstVisibleItemPosition()) {
//                mBannerPosition = mLayoutManager.findFirstVisibleItemPosition();
//                ArrayList<String> data = mBannerAdapter.getData();
//                String s = data.get(mBannerPosition % data.size());
//                GlideUtils.loadBlurry(mIvBanner, s);
//            }
//        }
//    });
//    mMainAdapter.addHeaderView(bannerView);
//}

///////////////////////////预留布局的沉浸式 start////////
//<View
//        android:id="@+id/status_bar_view"
//                android:layout_width="match_parent"
//                android:layout_height="0dp"
//                android:background="@color/colorPrimary" />
//@Override
//protected void initImmersionBar() {
//    super.initImmersionBar();
//    ImmersionBar.with(this).statusBarView(view)
//            .navigationBarColor(R.color.colorPrimary)
//            .keyboardEnable(true)
//            .init();
//}
///////////////////////////预留布局的沉浸式 end////////

///////////////////////////显示隐藏是否有//////////
// mTvStatus.setText(getText(getTitle(mTvStatus) + ImmersionBar.getStatusBarHeight(this)));
//         mTvHasNav.setText(getText(getTitle(mTvHasNav) + ImmersionBar.hasNavigationBar(this)));
//         mTvNav.setText(getText(getTitle(mTvNav) + ImmersionBar.getNavigationBarHeight(this)));
//         mTvNavWidth.setText(getText(getTitle(mTvNavWidth) + ImmersionBar.getNavigationBarWidth(this)));
//         mTvAction.setText(getText(getTitle(mTvAction) + ImmersionBar.getActionBarHeight(this)));
//         mTvHasNotch.post(() -> mTvHasNotch.setText(getText(getTitle(mTvHasNotch) + ImmersionBar.hasNotchScreen(this))));
//         mTvNotchHeight.post(() -> mTvNotchHeight.setText(getText(getTitle(mTvNotchHeight) + ImmersionBar.getNotchHeight(this))));
//         mTvFits.setText(getText(getTitle(mTvFits) + ImmersionBar.checkFitsSystemWindows(findViewById(android.R.id.content))));
//         mTvStatusDark.setText(getText(getTitle(mTvStatusDark) + ImmersionBar.isSupportStatusBarDarkFont()));
//         mTvNavigationDark.setText(getText(getTitle(mTvNavigationDark) + ImmersionBar.isSupportNavigationIconDark()));
//
//         if (!mIsHideStatusBar) {
//         ImmersionBar.hideStatusBar(getWindow());
//         mIsHideStatusBar = true;
//         } else {
//         ImmersionBar.showStatusBar(getWindow());
//         mIsHideStatusBar = false;
//         }