package com.liyiwei.thirdlibrary;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



public class BaseThirdLibActivity extends AppCompatActivity {
    private static final String TAG = "CustomBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseactivity_layout);


    }

}
