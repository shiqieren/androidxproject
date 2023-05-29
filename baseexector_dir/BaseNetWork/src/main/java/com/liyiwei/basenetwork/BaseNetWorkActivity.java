package com.liyiwei.basenetwork;


import android.annotation.SuppressLint;
import android.os.Bundle;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import hongyi.basenetwork.R;

public class BaseNetWorkActivity extends RxAppCompatActivity {
    private static final String TAG = "BaseNetWorkActivity";


    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

    }
}
