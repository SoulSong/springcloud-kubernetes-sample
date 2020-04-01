package com.shf.spring.kube.common.exception.handler;

import com.shf.spring.kube.common.exception.SentinelRestTemplateDegradeException;
import com.shf.spring.kube.common.exception.SentinelRestTemplateFlowException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/25 21:26
 */
@ControllerAdvice
public class SentinelRestTemplateExceptionHandler {

    @ExceptionHandler(SentinelRestTemplateFlowException.class)
    public ResponseEntity<String> restTemplateFlowExceptionHandler(SentinelRestTemplateFlowException exception) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("PS: have a rest.");
    }

    @ExceptionHandler(SentinelRestTemplateDegradeException.class)
    public ResponseEntity<String> restTemplateDegradeExceptionHandler(SentinelRestTemplateDegradeException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("PS: " + HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
    }
}
