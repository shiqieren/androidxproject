package com.liyiwei.basenetwork.baseretrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @创建者 CSDN_LQR
 * @描述 使用Retrofit对网络请求进行配置
 */
public class ApiRetrofit extends BaseApiRetrofit {

    public MyApi mApi;
    public static ApiRetrofit mInstance;

    private ApiRetrofit() {
        super();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //在构造方法中完成对Retrofit接口的初始化
        mApi = new Retrofit.Builder()
                .baseUrl(MyApi.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(MyApi.class);
    }

    public static ApiRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (ApiRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new ApiRetrofit();
                }
            }
        }
        return mInstance;
    }

    private RequestBody getRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);
        return body;
    }

    //登录
//    public Observable<LoginResponse> login(String region, String phone, String password) {
//        return mApi.login(getRequestBody(new LoginRequest(region, phone, password)));
//    }

    //token
//    public Observable<GetTokenResponse> getToken() {
//        return mApi.getToken();
//    }


    //查询
//    public Observable<GetUserInfoByIdResponse> getUserInfoById(String userid) {
//        return mApi.getUserInfoById(userid);
//    }

//    public Observable<GetUserInfoByPhoneResponse> getUserInfoFromPhone(String region, String phone) {
//        return mApi.getUserInfoFromPhone(region, phone);
//    }

//    public Observable<DismissGroupResponse> dissmissGroup(String groupId) {
//        return mApi.dissmissGroup(getRequestBody(new DismissGroupRequest(groupId)));
//    }

//    public Observable<GetUserInfosResponse> getUserInfos(List<String> ids) {
//        StringBuilder sb = new StringBuilder();
//        for (String s : ids) {
//            sb.append("id=");
//            sb.append(s);
//            sb.append("&");
//        }
//        String stringRequest = sb.substring(0, sb.length() - 1);
//        return mApi.getUserInfos(stringRequest);
//    }


}
