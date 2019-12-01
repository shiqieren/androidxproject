package com.liyiwei.basenetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.liyiwei.basenetwork.api.NBANoSingletonService;
import com.liyiwei.basenetwork.baseretrofit.entity.NBAJEntity;
import com.liyiwei.basenetwork.baserxjava.BaseObserver;
import com.liyiwei.basenetwork.utils.ApiConfig;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ：mp5a5 on 2019/1/7 14：03
 * @describe
 * @email：wwb199055@126.com
 */
public class TestActivity extends RxAppCompatActivity {

    private QuitAppReceiver mQuitAppReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initReceiver();


        new NBANoSingletonService()
                .getNBAInfo("720d49190f4dc5c15f5437b2a1320560")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<NBAJEntity>(this, true) {
                    @Override
                    public void onSuccess(NBAJEntity response) {
                        Toast.makeText(TestActivity.this, response.getData().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void initReceiver() {
        mQuitAppReceiver = new QuitAppReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ApiConfig.getQuitBroadcastReceiverFilter());
        registerReceiver(mQuitAppReceiver, filter);
    }


    private class QuitAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ApiConfig.getQuitBroadcastReceiverFilter().equals(intent.getAction())) {

                String msg = intent.getStringExtra(BaseObserver.TOKEN_INVALID_TAG);
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(TestActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

