package com.qtimes.data.bean;

/**
 * 网络请求返回的base
 * Created by liutao on 2017/1/3.
 */

public class BaseResponse<T> {
    private String code;
    private String response_code_string;
    private String msg;
    private T data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponse_code_string() {
        return response_code_string;
    }

    public void setResponse_code_string(String response_code_string) {
        this.response_code_string = response_code_string;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
