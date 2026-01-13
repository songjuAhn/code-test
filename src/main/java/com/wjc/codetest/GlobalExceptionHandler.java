package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
/*
 * 문제: [설계] 에러 처리 세분화 필요.
 * 원인: [코드] 무조건 INTERNAL_SERVER_ERROR 로 리턴하게 되어있음.
 * 개선안: ResponseStatus에 따른 세분화 처리 필요.
 *       ex. 데이터가 없는 경우 204 HttpStatus.NO_CONTENT로 처리하여 명확한 응답코드 리턴
 */
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
