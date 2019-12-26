package com.cool.mmc.common.config;

import com.core.common.R;
import com.core.exception.CoolException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by vincent on 2019-06-09
 */
@RestControllerAdvice
public class CoolExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return R.error();
    }

    @ExceptionHandler(CoolException.class)
    public R handleRRException(CoolException e) {
        return R.parse(e.getMessage());
    }

}
