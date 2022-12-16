package com.sparta.soundsea.common.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String msg;
    private int statusCode;

    public ExceptionResponse(ExceptionMessage exceptionMessage) {
        this.msg = exceptionMessage.getMsg();
        this.statusCode = exceptionMessage.getStatusCode();
    }

    public ExceptionResponse(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
