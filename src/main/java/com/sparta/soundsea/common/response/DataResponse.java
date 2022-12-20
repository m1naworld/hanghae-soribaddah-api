package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends Response{
    private T data;

    private boolean checkLoginId;

    public DataResponse(ResponseMessage responseMessage, T data) {
        super(responseMessage);
        this.data = data;
    }

    public DataResponse(ResponseMessage responseMessage, T data, boolean checkLoginId) {
        super(responseMessage);
        this.data = data;
        this.checkLoginId = checkLoginId;
    }

    public DataResponse(String msg, int statusCode, T data) {
        super(msg, statusCode);
        this.data = data;
    }
}
