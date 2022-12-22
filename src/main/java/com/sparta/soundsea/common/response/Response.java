package com.sparta.soundsea.common.response;

import lombok.Getter;

@Getter
public class Response {
    private String msg;
    private int statusCode;

    public Response(ResponseMessage responseMessage) {
        this.msg = responseMessage.getMsg();
        this.statusCode = responseMessage.getStatusCode();
    }

}
