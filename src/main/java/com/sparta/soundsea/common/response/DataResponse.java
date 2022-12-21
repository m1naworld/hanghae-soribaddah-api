package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends Response{
    private T data;

    private boolean checkLogin;

    public DataResponse(ResponseMessage responseMessage, T data) {
        super(responseMessage);
        this.data = data;
    }

    public DataResponse(ResponseMessage responseMessage, T data, boolean checkLogin) {
        super(responseMessage);
        this.data = data;
        this.checkLogin = checkLogin;
    }

    public DataResponse(String msg, int statusCode, T data) {
        super(msg, statusCode);
        this.data = data;
    }
}
