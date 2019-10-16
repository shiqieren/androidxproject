package com.liyiwei.appcrash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import hongyi.basemvpmode.R;

public class BaseAppCrashActivity extends AppCompatActivity {
    private static final String TAG = "BaseMVPmodeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_layout);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("点击异常");
            }
        });
        findViewById(R.id.thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        throw new RuntimeException("子线程异常");
                    }
                }.start();
            }
        });
        findViewById(R.id.handler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("handler异常");
                    }
                });
            }
        });

        findViewById(R.id.act).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseAppCrashActivity.this, SecondAct.class));
            }
        });

        findViewById(R.id.noact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseAppCrashActivity.this, UnknowAct.class));
            }
        });
        ////////黑屏测试//////////
        findViewById(R.id.newActOnCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onCreate");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActOnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onStart");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActOnReStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onRestart");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActOnResume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onResume");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActOnPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onPause");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActOnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onStop");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActonDestroy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "onDestroy");
                startActivity(intent);
            }
        });
        findViewById(R.id.newActFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseAppCrashActivity.this, LifecycleExceptionActivity.class);
                intent.putExtra(LifecycleExceptionActivity.METHOD, "finish");
                startActivity(intent);
            }
        });

    }

}
