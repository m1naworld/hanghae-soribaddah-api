package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends Response{
    private T data;

    private boolean checkLogin;

    public DataResponse(ResponseMessage responseMessage, T data, boolean checkLogin) {
        super(responseMessage);
        this.data = data;
        this.checkLogin = checkLogin;
    }

}
