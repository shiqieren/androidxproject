package com.liyiwei.baselibrary.util.uiutil;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.liyiwei.baselibrary.R;

public class LoadingViewUtil {

    /**
     * 显示加载圈
     *  @param activity
     * @param isCover      是否需要遮罩 防止点击
     * @param isShowLoadBg 是否需要小局部进度条背景底色
     * @param message
     */
    public static void showLoading(FragmentActivity activity, boolean isCover, boolean isShowLoadBg, String message) {
        if (isLoading(activity)) {
            return;
        }
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = LayoutInflater.from(activity).inflate(R.layout.common_loading, null);
            if (isCover) {
                //遮罩层设置点击事件，拦截底层视图的点击事件
                loadingView.findViewById(R.id.cover).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
            loadingView.findViewById(R.id.common_progress_bg).setVisibility(isShowLoadBg ? View.VISIBLE : View.GONE);
            loadingView.findViewById(R.id.cover).setVisibility(isCover ? View.VISIBLE : View.GONE);
            if (message!=null){
                ((TextView)loadingView.findViewById(R.id.message_tip)).setText(message);
                loadingView.findViewById(R.id.message_tip).setVisibility(View.VISIBLE);
            }else {
                loadingView.findViewById(R.id.message_tip).setVisibility(View.GONE);
            }
            root.removeView(loadingView);
            root.addView(loadingView);
        }
    }

    /**
     * 隐藏加载圈
     *
     * @param activity
     */
    public static void hideLoading(FragmentActivity activity) {
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = root.findViewById(R.id.cover_root);
            if (loadingView != null) {
                root.removeView(loadingView);
            }
        }
    }

    /**
     * 加载圈是否正在显示
     *
     * @param activity
     * @return
     */
    public static boolean isLoading(FragmentActivity activity) {
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = root.findViewById(R.id.cover_root);
            return loadingView != null && root.indexOfChild(loadingView) != -1;
        }
        return false;
    }
}
