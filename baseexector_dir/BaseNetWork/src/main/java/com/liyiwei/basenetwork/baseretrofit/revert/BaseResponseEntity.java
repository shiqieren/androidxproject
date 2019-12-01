package com.liyiwei.basenetwork.baseretrofit.revert;

import com.liyiwei.basenetwork.utils.ApiConfig;

import java.io.Serializable;

public class BaseResponseEntity implements Serializable {
    int code = 0;
    String msg = "";
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    private static final long serialVersionUID = 1L;
    public boolean success(){
        return ApiConfig.getSucceedCode() == code;
    }

    public boolean tokenInvalid(){
        return ApiConfig.getInvalidateToken() == code;
    }
}
