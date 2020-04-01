package com.shf.spring.kube.common.exception;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/25 21:18
 */
public class SentinelRestTemplateFlowException extends RuntimeException {

    public SentinelRestTemplateFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
