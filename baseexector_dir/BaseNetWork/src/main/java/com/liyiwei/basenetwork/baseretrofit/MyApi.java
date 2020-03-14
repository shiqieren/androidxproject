package com.liyiwei.basenetwork.baseretrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @创建者 CSDN_LQR
 * @描述 server端api
 */

public interface MyApi {

    public static final String BASE_URL = "http://api.sealtalk.im/";


    //登录---来源LQRWeChat项目
//    @POST("user/login")
//    Observable<LoginResponse> login(@Body RequestBody body);
    //----------------------举例
//    ApiRetrofit.getInstance().login(AppConst.REGION, phone, pwd)
//                .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(loginResponse -> {
//        int code = loginResponse.getCode();
//        mContext.hideWaitingDialog();
//        if (code == 200) {
//            UserCache.save(loginResponse.getResult().getId(), phone, loginResponse.getResult().getToken());
//            mContext.jumpToActivityAndClearTask(MainActivity.class);
//            mContext.finish();
//        } else {
//            loginError(new ServerException(UIUtils.getString(R.string.login_error) + code));
//        }
//    }, this::loginError);
    //--------------------------------------







//
//    //获取 token 前置条件需要登录   502 坏的网关 测试环境用户已达上限
//    @GET("user/get_token")
//    Observable<GetTokenResponse> getToken();
//
//    //根据 id 去服务端查询用户信息
//    @GET("user/{userid}")
//    Observable<GetUserInfoByIdResponse> getUserInfoById(@Path("userid") String userid);
//
//    //通过国家码和手机号查询用户信息
//    @GET("user/find/{region}/{phone}")
//    Observable<GetUserInfoByPhoneResponse> getUserInfoFromPhone(@Path("region") String region, @Path("phone") String phone);
//
//
//    //下载图片
//    @GET
//    Observable<ResponseBody> downloadPic(@Url String url);

}
