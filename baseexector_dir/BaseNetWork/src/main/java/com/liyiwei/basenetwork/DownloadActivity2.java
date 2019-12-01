package com.liyiwei.basenetwork;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.liyiwei.basenetwork.filemanager.DownloaderManager;
import com.liyiwei.basenetwork.utils.DownloadFileUtils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.io.IOException;

import hongyi.basenetwork.R;

public class DownloadActivity2 extends RxAppCompatActivity implements DownloaderManager.ProgressListener{
    private String urlPath = "http://gdown.baidu.com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk";
    private String[] permissions_storage = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    private int request_external_storage = 1;

    private DownloaderManager downloaderManager;

    private long breakPoints = 0L;
    private long totalBytes = 0L;
    private long contentLength = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();

        findViewById(R.id.iv_stop_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloaderManager.pause();
                breakPoints = totalBytes;
                setStopView();
            }
        });

        findViewById(R.id.bt_re_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloaderManager.downloadStartPoint(breakPoints);
                setDownloadView();
            }
        });

        findViewById(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloaderManager = new DownloaderManager(urlPath, DownloadActivity2.this);
                downloaderManager.downloadStartPoint(0L);
                setDownloadView();
            }
        });

    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否有这个权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                    .PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        this, permissions_storage, request_external_storage
                );
            } else {
                downloaderManager = new DownloaderManager(urlPath, this);
                downloaderManager.downloadStartPoint((0L));
                setDownloadView();
            }
        } else {
            downloaderManager = new DownloaderManager(urlPath, this);
            downloaderManager.downloadStartPoint(0L);
            setDownloadView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == request_external_storage && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloaderManager =new DownloaderManager(urlPath, this);
            downloaderManager.downloadStartPoint(0L);
            setDownloadView();
        } else {
            Toast.makeText(DownloadActivity2.this, "没有权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPreExecute(long contentLength) {
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;
            ((ProgressBar)findViewById(R.id.tv_download_progress)).setMax((int)(contentLength / 1024));

        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        ((ProgressBar)findViewById(R.id.tv_download_progress)).setProgress((int)((totalBytes + breakPoints) / 1024));
        String value = totalBytes + breakPoints + "/" + contentLength;
        ((TextView)findViewById(R.id.tv_pdf_progress)).setText(value);

        if (done) {
            //下载文件的路径
            try {
                String path = DownloadFileUtils.getDownloadPath(urlPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail() {
        breakPoints = 0L;
        totalBytes = 0L;
        setFailView();
    }

    private void setDownloadView() {
        findViewById(R.id.ll_download_set).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_re_download).setVisibility(View.GONE);
        findViewById(R.id.bt_download).setVisibility(View.GONE);
    }

    private void setStopView() {
        findViewById(R.id.ll_download_set).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_re_download).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_download).setVisibility(View.GONE);
    }

    private void setFailView() {
        findViewById(R.id.ll_download_set).setVisibility(View.GONE);
        findViewById(R.id.bt_re_download).setVisibility(View.GONE);
        findViewById(R.id.bt_download).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloaderManager.cancelDownload();
    }
}
