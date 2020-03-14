package com.liyiwei.baselibrary.base;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liyiwei.baselibrary.util.apputil.ActivityStack;
import com.liyiwei.baselibrary.util.uiutil.ToastUtils;
import com.liyiwei.baselibrary.util.utils.NetworkUtils;
import com.liyiwei.baselibrary.util.xutils.PermissionUtils;

import static com.liyiwei.baselibrary.base.BaseApplication.getContext;

public abstract class BaseActivity extends AppCompatActivity {
    private Dialog loadingDialog = null;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ActivityStack.getInstance().pushActivity(this);//将界面加入堆栈
        context = this;//复制上下文
    }

    public abstract int getContentView();

    public boolean getNetworkStatus(){
        return NetworkUtils.isNetworkConnected(this);
    }

    public Context getContext(){
        return context;
    }

    public void toast(String mess){
        ToastUtils.showShort(mess);
    }

    /**
     * 显示正在加载的提示
     * @param message
     */
    public void showLoadingDialog(String message){
//        loadingDialog = CustomWeiboDialogUtils.createLoadingDialog(this,message);
    }

    /**
     * 关闭正在显示的提示
     */
    public void closeLoadingDialog(){
//        CustomWeiboDialogUtils.closeDialog(loadingDialog);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            ActivityStack.getInstance().popActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().popActivity(this);
    }


    public void onPermissionSuccess(){

    }

    public void onPermissionError(String[] deniedPermissions){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    public void requestPermission(final int requestCode, final String[] permission){
        PermissionUtils.requestPermissions(getContext(), requestCode, permission, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                onPermissionSuccess();
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                onPermissionError(deniedPermissions);
            }
        });
    }

}
