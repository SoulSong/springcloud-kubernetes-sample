package com.shf.spring.kube.feign.sentinel;

/**
 * description :
 * Sentinel supports fallback for open-feign.
 *
 * @author songhaifeng
 * @date 2020/3/21 19:55
 */
public class SentinelMissClientFallback implements SentinelMissClient {

    private Throwable throwable;

    public SentinelMissClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String miss() {
        throwable.printStackTrace();
        return "Hello, fallback via the sentinel with openfeign.";
    }
}

