package com.shf.spring.kube.feign.sentinel;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/21 21:49
 */
@Component
public class SentinelMissClientFallbackFactory implements FallbackFactory<SentinelMissClientFallback> {

    @Override
    public SentinelMissClientFallback create(Throwable cause) {
        return new SentinelMissClientFallback(cause);
    }
}
