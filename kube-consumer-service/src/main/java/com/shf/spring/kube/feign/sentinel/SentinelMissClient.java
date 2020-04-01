package com.shf.spring.kube.feign.sentinel;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Mock as a feign client, fallback via the sentinel.
 *
 * @author songhaifeng
 */
@FeignClient(name = "miss-client", fallbackFactory = SentinelMissClientFallbackFactory.class)
public interface SentinelMissClient {
    @GetMapping(value = "miss")
    String miss();
}

