package com.sparta.soundsea.security.jwt.exception;

import com.sparta.soundsea.common.exception.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomSecurityException extends RuntimeException {
    private final ExceptionMessage exceptionMessage;
}
