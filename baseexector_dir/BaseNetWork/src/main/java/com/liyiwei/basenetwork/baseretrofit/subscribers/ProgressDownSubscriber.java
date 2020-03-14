package com.liyiwei.basenetwork.baseretrofit.subscribers;


import android.os.Handler;


import com.liyiwei.basenetwork.baseretrofit.download.DownInfo;
import com.liyiwei.basenetwork.baseretrofit.download.DownLoadListener.DownloadProgressListener;
import com.liyiwei.basenetwork.baseretrofit.download.DownState;
import com.liyiwei.basenetwork.baseretrofit.download.HttpDownManager;
import com.liyiwei.basenetwork.baseretrofit.listener.HttpDownOnNextListener;
import com.liyiwei.basenetwork.utils.DbDownUtil;

import java.lang.ref.SoftReference;

import rx.Subscriber;

/**
 * 断点下载处理类Subscriber
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class ProgressDownSubscriber<T> extends Subscriber<T> implements DownloadProgressListener {
    @Override
    public void update(long read, long count, boolean done) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
//    //弱引用结果回调
//    private SoftReference<HttpDownOnNextListener> mSubscriberOnNextListener;
//    /*下载数据*/
//    private DownInfo downInfo;
//    private Handler handler;
//
//
//    public ProgressDownSubscriber(DownInfo downInfo,Handler handler) {
//        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
//        this.downInfo=downInfo;
//        this.handler=handler;
//    }
//
//
//    public void setDownInfo(DownInfo downInfo) {
//        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
//        this.downInfo=downInfo;
//    }
//
//
//    /**
//     * 订阅开始时调用
//     * 显示ProgressDialog
//     */
//    @Override
//    public void onStart() {
//        if(mSubscriberOnNextListener.get()!=null){
//            mSubscriberOnNextListener.get().onStart();
//        }
//        downInfo.setState(DownState.START);
//    }
//
//    /**
//     * 完成，隐藏ProgressDialog
//     */
//    @Override
//    public void onCompleted() {
//        if(mSubscriberOnNextListener.get()!=null){
//            mSubscriberOnNextListener.get().onComplete();
//        }
//        HttpDownManager.getInstance().remove(downInfo);
//        downInfo.setState(DownState.FINISH);
//        DbDownUtil.getInstance().update(downInfo);
//    }
//
//    /**
//     * 对错误进行统一处理
//     * 隐藏ProgressDialog
//     *
//     * @param e
//     */
//    @Override
//    public void onError(Throwable e) {
//        if(mSubscriberOnNextListener.get()!=null){
//            mSubscriberOnNextListener.get().onError(e);
//        }
//        HttpDownManager.getInstance().remove(downInfo);
//        downInfo.setState(DownState.ERROR);
//        DbDownUtil.getInstance().update(downInfo);
//    }
//
//    /**
//     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
//     *
//     * @param t 创建Subscriber时的泛型类型
//     */
//    @Override
//    public void onNext(T t) {
//        if (mSubscriberOnNextListener.get() != null) {
//            mSubscriberOnNextListener.get().onNext(t);
//        }
//    }
//
//    @Override
//    public void update(long read, long count, boolean done) {
//        if (downInfo.getCountLength() > count) {
//            read = downInfo.getCountLength() - count + read;
//        } else {
//            downInfo.setCountLength(count);
//        }
//        downInfo.setReadLength(read);
//
//        if (mSubscriberOnNextListener.get() == null || !downInfo.isUpdateProgress()) return;
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
//                if (downInfo.getState() == DownState.PAUSE || downInfo.getState() == DownState.STOP) return;
//                downInfo.setState(DownState.DOWN);
//                mSubscriberOnNextListener.get().updateProgress(downInfo.getReadLength(), downInfo.getCountLength());
//            }
//        });
//    }

}