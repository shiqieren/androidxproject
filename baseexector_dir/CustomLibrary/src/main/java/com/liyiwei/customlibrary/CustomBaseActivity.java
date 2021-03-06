package com.liyiwei.customlibrary;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import liyiwei.customlibrary.R;


public class CustomBaseActivity extends AppCompatActivity {
    private static final String TAG = "CustomBaseActivity";
    private int position = 0;
    private String string;

    private TextView textView01;
    private TextView textView02;
    private TextView textView03;
    private TextView textView04;
    private TextView textView05;
    private TextView textView06;
    private TextView textView07;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView01 = (TextView) findViewById(R.id.text01);
        textView02 = (TextView) findViewById(R.id.text02);
        textView03 = (TextView) findViewById(R.id.text03);
        textView04 = (TextView) findViewById(R.id.text04);
        textView06 = (TextView) findViewById(R.id.text06);
        textView07 = (TextView) findViewById(R.id.text07);
        String string01 = textView01.getText().toString();
        String string02 = textView02.getText().toString();
        String string03 = textView03.getText().toString();
        String string04 = textView04.getText().toString();
        String string06 = textView06.getText().toString();
        textView05 = (TextView) findViewById(R.id.text05);
        string = textView05.getText().toString();

        SpannableString spannableString01 = new SpannableString(string01);

        ForegroundColorSpan colorSpan01 = new ForegroundColorSpan(Color.parseColor("#0099FF"));
        spannableString01.setSpan(colorSpan01, 7, spannableString01.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.3f);
        spannableString01.setSpan(sizeSpan01, 7, spannableString01.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan styleSpan01 = new StyleSpan(Typeface.BOLD);
        spannableString01.setSpan(styleSpan01, 7, spannableString01.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView01.setText(spannableString01);

        SpannableString spannableString02 = new SpannableString(string02);

        URLSpan urlSpan = new URLSpan("http://www.jianshu.com/users/dbae9ac95c78");
        textView02.setHighlightColor(Color.parseColor("#16646464"));
        spannableString02.setSpan(urlSpan, 9, spannableString02.length() - 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan02 = new ForegroundColorSpan(Color.parseColor("#0099FF"));
        spannableString02.setSpan(colorSpan02, 9, spannableString02.length() - 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView02.setText(spannableString02);
        textView02.setMovementMethod(LinkMovementMethod.getInstance());

//
//        UnderlineSpan underlineSpan02 = new UnderlineSpan();
//        spannableString02.setSpan(underlineSpan02, 9, spannableString02.length() - 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString spannableString03 = new SpannableString(string03);

        ForegroundColorSpan colorSpan03 = new ForegroundColorSpan(Color.parseColor("#EE0000"));
        spannableString03.setSpan(colorSpan03, 0, spannableString03.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.2f);
        spannableString03.setSpan(sizeSpan03, 1, spannableString03.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan styleSpan03 = new StyleSpan(Typeface.BOLD);
        spannableString03.setSpan(styleSpan03, 1, spannableString03.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView03.setText(spannableString03);

        SpannableString spannableString04 = new SpannableString(string04);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString04.setSpan(strikethroughSpan, 0, spannableString04.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView04.setText(spannableString04);



        SpannableString spannableString06 = new SpannableString(string06);
        ClickableSpan clickableSpan = new MyClickableSpan("123456789");
        ForegroundColorSpan colorSpan04 = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#0099EE"));
        spannableString06.setSpan(colorSpan04, 2, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString06.setSpan(clickableSpan, 2, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString06.setSpan(backgroundColorSpan, 2, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView06.setText(spannableString06);
        textView06.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString spannableString07 = new SpannableString("22 + 32 = 13");
        SuperscriptSpan superscriptSpan01 = new SuperscriptSpan();
        SuperscriptSpan superscriptSpan02 = new SuperscriptSpan();
        spannableString07.setSpan(superscriptSpan01, 1, 2 ,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString07.setSpan(superscriptSpan02, 6, 7 ,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(0.5f);
        RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(0.5f);
        spannableString07.setSpan(sizeSpan05, 1, 2 ,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString07.setSpan(sizeSpan06, 6, 7 ,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView07.setText(spannableString07);

    }
    class MyClickableSpan extends ClickableSpan {

        private String content;

        public MyClickableSpan(String content) {
            this.content = content;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {

        }
    }

}
