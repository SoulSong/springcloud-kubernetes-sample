package com.shf.spring.kube.service.impl;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/21 21:16
 */
public class SentinelCommonFallback {

    /**
     * Must be static method.
     *
     * @param throwable throwable
     * @return str
     */
    public static String commonFallback(Throwable throwable) {
        throwable.printStackTrace();
        return "fallback via the SentinelCommonFallback class";
    }

}
