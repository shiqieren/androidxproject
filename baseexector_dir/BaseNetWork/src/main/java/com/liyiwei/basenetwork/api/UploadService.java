package com.liyiwei.basenetwork.api;

import com.liyiwei.basenetwork.baseretrofit.RetrofitFactory;
import com.liyiwei.basenetwork.baseretrofit.entity.UploadEntity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

import java.util.List;

/**
 * @author ：mp5a5 on 2019/1/2 15：38
 * @describe
 * @email：wwb199055@126.com
 */
public class UploadService {

    private final UploadApi mUploadApi;

    private UploadService() {
        String url = "http://fnw-api-nginx-fnw-test.topaas.enncloud.cn/";
        mUploadApi = RetrofitFactory.getInstance().create(url,UploadApi.class);
    }

    public static UploadService getInstance() {
        return UploadServiceHolder.S_INSTANCE;
    }

    private static class UploadServiceHolder {
        private static final UploadService S_INSTANCE = new UploadService();
    }

    public Observable<UploadEntity> uploadPic(List<MultipartBody.Part> picList) {
        return mUploadApi.postUpload(picList);
    }

}
