package com.liyiwei.basenetwork.baseretrofit.flowable;

import io.reactivex.FlowableSubscriber;

public abstract class RxSubscribe<T> implements FlowableSubscriber<T> {
//    private Context context;
//    private LoadingDialog dialog;
//    private boolean isShowLoading = false;
//    private boolean isShowError = true;
//
//    public abstract void onSuccess(T t) throws Exception;
//
//   	public RxSubscribe(Context context) {
//        this(context, false);
//    }
//
//    public RxSubscribe(Context context, @StringRes int msg) {
//        this.context = context;
//        this.isShowLoading = true;
//        dialog = new LoadingDialog(context);
//        dialog.setMessage(context.getString(msg));
//    }
//
//    public RxSubscribe(Context context, String msg) {
//        this.context = context;
//        this.isShowLoading = true;
//        dialog = new LoadingDialog(context);
//        dialog.setMessage(msg);
//    }
//
//    public RxSubscribe(Context context, boolean isShowLoading) {
//        this.context = context;
//        this.isShowLoading = isShowLoading;
//        if (context != null) {
//            dialog = new LoadingDialog(context);
//            dialog.setMessage(context.getString(R.string.loading));
//        }
//    }
//
//    public RxSubscribe(Context context, boolean isShowLoading, boolean isShowError) {
//        this.context = context;
//        this.isShowLoading = isShowLoading;
//        this.isShowError = isShowError;
//        if (context != null) {
//            dialog = new LoadingDialog(context);
//            dialog.setMessage(context.getString(R.string.loading));
//        }
//    }
//
//    @Override
//    public void onSubscribe(Subscription s) {
//        s.request(Long.MAX_VALUE);
//        if (isShowLoading && dialog != null)
//            dialog.show();
//    }
//
//    @Override
//    public void onNext(T t) {
//        try {
//            onSuccess(t);
//        } catch (Exception e) {
//            onError(e);
//        }
//    }
//
//    private void showToast(int msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//    }
//
//    private void showToast(String msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onError(Throwable t) {
//        try {
//            if (t instanceof NoDataException) {
//                Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//                String name = type.toString();
//                try {
//                    name = name.replace("class ", "");
//                    Class<T> tClass = (Class<T>) Class.forName(name);
//                    T tc = tClass.newInstance();
//                    onSuccess(tc);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (t instanceof EmptyException || t instanceof UnknownHostException) {
//                onErrorView(t);
//            } else if (t instanceof NoMoreDataException) {
//                noMoreData();
//            } else if (t instanceof SocketTimeoutException) {
//                if (isShowError) {
//                    showToast(R.string.error_socket_time_out);
//                }
//            } else if (t instanceof ErrorException) {
//                //msgCode=200为成功，msgCode=201为失败，
//                // msgCode=202为参数错误，msgCode=400为未登录，
//                // msgCode=401为没有权限，msgCode=404为找不到资源，
//                // msgCode=500为服务器异常，msgCode>=3000的提示出来
//                ErrorException e = (ErrorException) t;
//                if (e.getCode() == 201) {
////                    showToast(e.getMessage());
//                } else if (e.getCode() == 202) {
//                    showToast(R.string.error_params_error);
//                } else if (e.getCode() == 400) {
//                    showToast(R.string.error_no_login);
//                } else if (e.getCode() == 401) {
//                    showToast(R.string.error_no_permission);
//                } else if (e.getCode() == 404) {
//                    showToast(R.string.error_404);
//                } else if (e.getCode() == 500) {
//                    showToast(R.string.error_service_error);
//                } else if (e.getCode() >= 3000){
//                    showToast(e.getMessage());
//                }
//            } else {
////                showToast(R.string.error_service_error);
////                showToast(t.getMessage());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onComplete() {
//        if (dialog != null)
//            dialog.dismiss();
//    }
//
//    public void noMoreData() {
//    }
//
//    public void onErrorView(Throwable t) {
//    }

}
