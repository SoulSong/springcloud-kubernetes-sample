package com.shf.spring.kube.common.exception;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/26 2:00
 */
public class SentinelRestTemplateDegradeException extends RuntimeException {

    public SentinelRestTemplateDegradeException(String message, Throwable cause) {
        super(message, cause);
    }
}