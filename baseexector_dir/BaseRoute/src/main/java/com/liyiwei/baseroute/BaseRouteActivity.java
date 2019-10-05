package com.liyiwei.baseroute;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import hongyi.baseroute.R;

public class BaseRouteActivity extends AppCompatActivity {
    private static final String TAG = "BaseNetWorkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseactivity_layout);


    }

}
