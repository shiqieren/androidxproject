package com.liyiwei.basenetwork.api;

import android.util.ArrayMap;

import com.liyiwei.basenetwork.baseretrofit.entity.NBAJEntity;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @author ：mp5a5 on 2019/1/16 10：08
 * @describe
 * @email：wwb199055@126.com
 */
public interface NbaJavaApi {

    @GET("wxarticle/chapters/json")
    Observable<NBAJEntity> getJONBAInfo(@QueryMap ArrayMap<String, Object> map);

    @GET("wxarticle/chapters/json")
    Flowable<NBAJEntity> getNBAJFTInfo(@QueryMap ArrayMap<String, Object> map);
    //----------------------------------------------------------------------------------------------//
    /**
     * method：网络请求的方法（区分大小写）
     * path：网络请求地址路径
     * hasBody：是否有请求体
     */
    @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
    Call<ResponseBody> getCall(@Path("id") int id);
    // {id} 表示是一个变量
    // Call 接受返回对象
    // method 的值 retrofit 不会做处理，所以要自行保证准确
    //----------------------------------------------------------------------------------------------//
    /**
     *表明是一个表单格式的请求（Content-Type:application/x-www-form-urlencoded）
     * <code>Field("username")</code> 表示将后面的 <code>String name</code> 中name的取值作为 username 的值
     * 1、@FormUrlEncoded
     * 作用：表示发送form-encoded的数据
     */
    @POST("/form")
    @FormUrlEncoded
    Call<ResponseBody> testFormUrlEncoded1(@Field("username") String name, @Field("age") int age);

    /**
     * {@link Part} 后面支持三种类型，{@link RequestBody}、{@link okhttp3.MultipartBody.Part} 、任意类型
     * 除 {@link okhttp3.MultipartBody.Part} 以外，其它类型都必须带上表单字段({@link okhttp3.MultipartBody.Part} 中已经包含了表单字段的信息)，
     * 2、@Multipart
     * 作用：表示发送form-encoded的数据（适用于 有文件 上传的场景）
     */
    @POST("/form")
    @Multipart
    Call<ResponseBody> testFileUpload1(@Part("name") RequestBody name, @Part("age") RequestBody age, @Part MultipartBody.Part file);

    //----------------------------------------------------------------------------------------------//
    // @Header
    @GET("user")
    Call<ResponseBody> getUser(@Header("Authorization") String authorization);

    // @Headers
    @Headers("Authorization: authorization")
    @GET("user")
    Call<ResponseBody> getUser();

    // 以上的效果是一致的。
    // 区别在于使用场景和使用方式
    /**
     *      1. 使用场景：@Header用于添加不固定的请求头，@Headers用于添加固定的请求头
     *      2. 使用方式：@Header作用于方法的参数；@Headers作用于方法
     */
    //
    //----------------------------------------------------------------------------------------------//
    /**
     //    @Body
     //    作用：以 Post方式 传递 自定义数据类型 给服务器,可以传输json文件
     //    特别注意：如果提交的是一个Map，那么作用相当于 @Field
     */


    //POST 网络请求， RequestBody来实现传输JSON文件
    @POST("rest/basis/modifyCrossMonthSubtractionContract")
    Call<ResponseBody> modifyCrossMonthSubtractionContract(@Body RequestBody body);

    //具体实现
    //    String d = GsonUtil.toJson(addOrEditMonth);
    //    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),GsonUtil.toJson(addOrEditMonth));
    //    Call<ResponseBody> call2 = service.modifyCrossMonthSubtractionContract(body );

    //----------------------------------------------------------------------------------------------//
    /**
     //    @Field & @FieldMap
     //    作用：发送 Post请求 时提交请求的表单字段
     //    具体使用：与 @FormUrlEncoded 注解配合使用
     */

    public interface MovieService {
        /**
         *表明是一个表单格式的请求（Content-Type:application/x-www-form-urlencoded）
         * <code>Field("username")</code> 表示将后面的 <code>String name</code> 中name的取值作为 username 的值
         */
        @POST("/form")
        @FormUrlEncoded
        Call<ResponseBody> testFormUrlEncoded1(@Field("username") String name, @Field("age") int age);

        /**
         * Map的key作为表单的键
         */
        @POST("/form")
        @FormUrlEncoded
        Call<ResponseBody> testFormUrlEncoded2(@FieldMap Map<String, Object> map);

    }


    /**
     // 具体使用
     // @Field
     // Call<ResponseBody> call1 = service.testFormUrlEncoded1("Carson", 24);
     */
    // @FieldMap
    // 实现的效果与上面相同，但要传入Map
    //      Map<String, Object> map = new HashMap<>();
    //      map.put("username", "Carson");
    //      map.put("age", 24);
    //      Call<ResponseBody> call2 = service.testFormUrlEncoded2(map);
    //----------------------------------------------------------------------------------------------//
    /**
     //4、@Url
     //作用：直接传入一个请求的 URL变量 用于URL设置
     */

    @GET
    Call<ResponseBody> testUrl(@Url String ur);
    // 当有URL注解时，@GET传入的URL就可以省略
    // 当GET、POST...HTTP等方法中没有设置Url时，则必须使用 {@link Url}提供

    //用法
    //    String url = "/sso/checkregist/" + etLoginName.getText().toString();
    //    Call<ResponseBody> call2 = service.testUrl(map);
    /**
     //    5、 @Path
     //    作用：URL地址的缺省值
     */

    //    @GET("users/{user}/repos")
    //    Call<ResponseBody>  testPath（@Path("user") String user ）;
    // 访问的API是：https://api.github.com/users/{user}/repos
    // 在发起请求时， {user} 会被替换为方法的第一个参数 user（被@Path注解作用）
    //----------------------------------------------------------------------------------------------//
    /**
     //    6、@Part & @PartMap
     //    作用：发送 Post请求 时提交请求的表单字段与@Field的区别：功能相同，但携带的参数类型更加丰富，包括数据流，所以适用于 有文件上传 的场景
     //    具体使用：与 @Multipart 注解配合使用
     */

    /**
     //上传单个文件
     */
    //    @POST("/form")
    //    @Multipart
    //    Call<ResponseBody> testFileUpload1(@Part("name") RequestBody name, @Part("age") RequestBody age, @Part MultipartBody.Part file);

    /**
     //上传单个文件
     */
    @POST("/form")
    @Multipart
    Call<ResponseBody> testFileUpload2(@PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);

    /**
     //上传多个文件
     */

    @POST("/form")
    @Multipart
    Call<ResponseBody> testFileUpload3(@PartMap Map<String, RequestBody> args);

    // 具体使用
    MediaType textType = MediaType.parse("text/plain");
    RequestBody name = RequestBody.create(textType, "Carson");
    RequestBody age = RequestBody.create(textType, "24");
    RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "这里是模拟文件的内容");

    /**
     // @Part
     */

    //    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
    //    Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);
    //    ResponseBodyPrinter.printResponseBody(call3);

    /**
     // @PartMap
     // 实现和上面同样的效果
     */
    //    Map<String, RequestBody> fileUpload2Args = new HashMap<>();
    //    fileUpload2Args.put("name", name);
    //    fileUpload2Args.put("age", age);
        //这里并不会被当成文件，因为没有文件名(包含在Content-Disposition请求头中)，但上面的 filePart 有
        //fileUpload2Args.put("file", file);
    //    Call<ResponseBody> call4 = service.testFileUpload2(fileUpload2Args, filePart); //单独处理文件
    //    ResponseBodyPrinter.printResponseBody(call4);

    //----------------------------------------------------------------------------------------------//
    /**
     //7、@Query和@QueryMap
     //    //    作用：用于 @GET 方法的查询参数（Query = Url 中 ‘?’ 后面的 key-value）
     //    //    具体使用：配置时只需要在接口方法中增加一个参数即可：
     */
    @GET("/")
    Call<String> cate(@Query("cate") String cate);

    @GET("News")
    Call<Response> getItem(@QueryMap Map<String, String> map);
    // 其使用方式同 @Field与@FieldMap
    //----------------------------------------------------------------------------------------------//
    //发送网络请求(异步)
    //    call.enqueue(new Callback<Translation>() {
    //        //请求成功时回调
    //        @Override
    //        public void onResponse(Call<Translation> call, Response<Translation> response) {
    //            //请求处理,输出结果
    //            response.body().show();
    //        }
    //
    //        //请求失败时候的回调
    //        @Override
    //        public void onFailure(Call<Translation> call, Throwable throwable) {
    //            System.out.println("连接失败");
    //        }
    //    });

    // 发送网络请求（同步）
    //    Response<Reception> response = call.execute();
    // 对返回数据进行处理
    //response.body().show();
    //----------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------//

}
