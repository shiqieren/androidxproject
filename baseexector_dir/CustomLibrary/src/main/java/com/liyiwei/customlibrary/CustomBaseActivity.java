package com.liyiwei.customlibrary;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.liyiwei.baseresource.R;

public class CustomBaseActivity extends AppCompatActivity {
    private static final String TAG = "CustomBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseactivity_layout);


    }

}
