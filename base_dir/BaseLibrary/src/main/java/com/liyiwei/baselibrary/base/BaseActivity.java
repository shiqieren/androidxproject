package com.liyiwei.baselibrary.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.liyiwei.baselibrary.parallax.ParallaxHelper;

import com.liyiwei.baselibrary.util.apputil.AppManager;
import com.liyiwei.baselibrary.util.uiutil.LoadingViewUtil;
import com.liyiwei.baselibrary.util.uiutil.ToastUtils;
import com.liyiwei.baselibrary.util.utils.NetworkUtils;
import com.liyiwei.baselibrary.util.xutils.PermissionUtils;

public abstract class BaseActivity extends AppCompatActivity {
    private Context context;
    protected static final int PERMISSION_REQUEST_CODE = 3999;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        AppManager.getInstance().addActivity(this);//将界面加入堆栈
        context = this;//复制上下文
    }

    public abstract int getContentView();

    public boolean getNetworkStatus(){
        return NetworkUtils.isNetworkConnected(this);
    }

    public Context getContext(){
        return context;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            AppManager.getInstance().removeActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    public void disableBack() {
        ParallaxHelper.getInstance().getParallaxBackLayout(this).setEnableGesture(false);
    }
    ////////////////////////////loading+toast//////////////
    public void toast(String mess){
        ToastUtils.showShort(mess);
    }

    /**
     * 显示正在加载的提示
     * @param message
     */
    protected void showLoadingView(boolean isCover,boolean isShowLoadBg,String message){
        LoadingViewUtil.showLoading(this,isCover,isShowLoadBg,message);
    }
    /**
     * 关闭正在显示的提示
     */
    protected void hideLoadingView(){
        LoadingViewUtil.hideLoading(this);
    }

    protected void isLoading(){
        LoadingViewUtil.isLoading(this);
    }

    /////////////////////权限start/////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    private static final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("相机权限、读写权限")
                .setMessage("由于需要摄像，需要开启相机权限\n" +
                        "存储文件，需要开启读写权限\n" +
                        "否则无法正常使用")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(PERMISSION_REQUEST_CODE,permissions);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
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

    public void onPermissionSuccess(){

    }

    public void onPermissionError(String[] deniedPermissions){
        showDialogTipUserGoToAppSettting();
    }

    // 提示用户去应用设置界面手动开启权限
    public void showDialogTipUserGoToAppSettting() {
        new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，手动开启权限")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }
    /////////////////////权限end/////////////////////////////////
}
