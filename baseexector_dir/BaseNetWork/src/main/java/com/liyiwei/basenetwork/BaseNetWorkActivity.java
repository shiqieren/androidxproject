package com.liyiwei.basenetwork;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liyiwei.basenetwork.api.NBANoSingletonService;
import com.liyiwei.basenetwork.api.NbaService;
import com.liyiwei.basenetwork.api.UploadService;
import com.liyiwei.basenetwork.baseretrofit.entity.NBAJEntity;
import com.liyiwei.basenetwork.baseretrofit.revert.BaseResponseEntity;
import com.liyiwei.basenetwork.baserxjava.BaseDisposableObserver;
import com.liyiwei.basenetwork.baserxjava.BaseObserver;
import com.liyiwei.basenetwork.filemanager.UploadManager;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hongyi.basenetwork.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BaseNetWorkActivity extends RxAppCompatActivity {
    private static final String TAG = "BaseNetWorkActivity";
    private List<File> list = new ArrayList<File>();
    private CompositeDisposable mCompositeDisposable = null;

    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        mCompositeDisposable = new CompositeDisposable();

        //nba
        nbaNetwork();

        //自己管理网络
        ownerNetwork();

        //单例创建网络
        SingletonNetwork();

        //选择图片
        picTack();

        //上传图片
        uploadNetwork();

        //下载界面
        findViewById(R.id.btnDownload).setOnClickListener(v -> startActivity(new Intent(this, DownloadActivity2.class)));

    }

    private void uploadNetwork() {
        findViewById(R.id.btnUpload).setOnClickListener(v -> {
            UploadManager.getInstance()
                    .uploadMultiPicList(list)
                    .subscribe(parts -> {
                        UploadService.getInstance()
                                .uploadPic(parts)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(this.bindToLifecycle())
                                .subscribe(new BaseObserver<BaseResponseEntity>(this, true) {
                                    @Override
                                    public void onSuccess(BaseResponseEntity response) {
                                        Toast.makeText(BaseNetWorkActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        //super.onError(e);
                                        onRequestEnd();
                                        Toast.makeText(BaseNetWorkActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    });

        });
    }

    private void picTack() {
        findViewById(R.id.btnChoose).setOnClickListener(v -> {

//            new RxPermissions(this)
//                    .requestEach(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(it -> {
//                        if (it.granted) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, 1);
                            }catch (Exception e){
                                Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
                            }
//                        }
//                        if (it.shouldShowRequestPermissionRationale) {
//                            Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
//                        }
//                    });
        });
    }

    private void SingletonNetwork() {
        findViewById(R.id.tvTest).setOnClickListener(v -> {
            new NBANoSingletonService()
                    .getNBAInfo("720d49190f4dc5c15f5437b2a1320560")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .subscribe(new BaseObserver<NBAJEntity>(this, true) {
                        @Override
                        public void onSuccess(NBAJEntity response) {
                            Toast.makeText(BaseNetWorkActivity.this, response.getData().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void ownerNetwork() {
        findViewById(R.id.btnOwner).setOnClickListener(v -> {
            BaseDisposableObserver<NBAJEntity> disposable = NbaService.getInstance()
                    .getJONBAInfo("720d49190f4dc5c15f5437b2a1320560")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableObserver<NBAJEntity>(this, true) {
                        @Override
                        public void onSuccess(NBAJEntity response) {
                            Toast.makeText(BaseNetWorkActivity.this, response.getData().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

           /* BaseDisposableSubscriber<NBAKTEntity> disposable = NbaService.getInstance()
                    .getJFNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                    .compose(RxTransformerUtils.flowableTransformer())
                    .subscribeWith(new BaseDisposableSubscriber<NBAKTEntity>(this, true) {
                        @Override
                        public void onSuccess(NBAKTEntity response) {
                            Toast.makeText(MainActivity.this, response.getResult().getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    });*/

            mCompositeDisposable.add(disposable);

        });
    }

    private void nbaNetwork() {
        findViewById(R.id.btnNBA).setOnClickListener(v -> {
            NbaService.getInstance()
                    .getJONBAInfo("720d49190f4dc5c15f5437b2a1320560")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .subscribe(new BaseObserver<NBAJEntity>(this, true) {
                        @Override
                        protected void onRequestStart() {
                            super.onRequestStart();
                            Log.e("MainActivity", "请求开始");
                        }

                        @Override
                        protected void onRequestEnd() {
                            super.onRequestEnd();
                            Log.e("MainActivity", "请求结束");
                        }

                        @Override
                        public void onSuccess(NBAJEntity response) {
                            Log.e("MainActivity", "请求成功");
                            Toast.makeText(BaseNetWorkActivity.this, response.getData().toString(), Toast.LENGTH_SHORT).show();
                        }

                    });


        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (1 == requestCode) {
                Uri uri = Objects.requireNonNull(data).getData();
                String[] arrayOf = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(Objects.requireNonNull(uri), arrayOf, null, null, null);
                if (Objects.requireNonNull(cursor).moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String path = cursor.getString(columnIndex);
                    File file = null;
                    try {
                        file = new File(path);
                        list.add(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
            Toast.makeText(this, "选取了：" + list.size() + " 张照片", Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
