package com.liyiwei.baselibrary.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.liyiwei.baselibrary.util.uiutil.ToastUtils;
import com.liyiwei.baselibrary.util.utils.NetworkUtils;

public abstract class BaseFragment extends Fragment {
    private Dialog loadingDialog = null;
    private Activity mActivity;
    public boolean getNetworkStatus(){
        return NetworkUtils.isNetworkConnected(getActivity());
    }

    public Context getContext(){
        return getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    public void toast(String mess){
        ToastUtils.showShort(mess);
    }

    /**
     * 显示正在加载的提示
     * @param message
     */
    public void showLoadingDialog(String message){
//        loadingDialog = CustomWeiboDialogUtils.createLoadingDialog(getContext(),message);
    }

    /**
     * 关闭正在显示的提示
     */
    public void closeLoadingDialog(){
//        CustomWeiboDialogUtils.closeDialog(loadingDialog);
    }
}
