package com.sparta.soundsea.user.exception;

import com.sparta.soundsea.common.exception.ExceptionResponse;
import com.sparta.soundsea.user.service.UserService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 1. Componet 혹은 Bean의 로드 순서를 정의. Exception이니까 가장 먼저 불러옴
@Order(Ordered.HIGHEST_PRECEDENCE)
// 2. RestControllerAdvice: @ContollerAdvice + @ResponseBody
// 2-1. @ControllerAdvice: @InitBinder, @ModelAttribute, @ExceptionHandler 관련 어노테이션을 여러 컨트롤러에 걸쳐 공통으로 설정 할 수 있게 해주는 어노테이션
// 2-2. assignableTypes: ExceptionHandler가 적용되는 범위를 지정, 조건을 과하게 걸면 성능에 매우 안좋은 영향을 끼칠 수 있다.
@RestControllerAdvice(assignableTypes = UserService.class)
public class LoginExceptionHandler {
    // 3. Response Status 설정 (Header)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // 4. ExceptionHandler가 적용될 Exception의 종류를 명시
    // 4-1. IllegalArgumentException : 적합하지 않거나(illegal) 적절하지 못한(inappropriate) 인자를 메소드에 넘겨주었을 때 발생
    //      아이디 존재하지 않음, 패스워드 불일치 등
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse handleBadRequest(Exception e){
        return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}
