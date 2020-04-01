package com.shf.spring.kube.consumer.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description :
 * Test integration with sentinel
 *
 * @author songhaifeng
 * @date 2020/3/21 16:52
 */
@RequestMapping(value = "/sentinel")
public interface SentinelEndpoint {

    @GetMapping(value = "hello")
    String hello();

    @GetMapping(value = "limit-handler")
    String limitHandler();

    @GetMapping(value = "limit-common-handler")
    String limitCommonHandler();

    @GetMapping(value = "fallback-via-feign-client")
    String fallbackViaFeignClient();

    @GetMapping(value = "fallback-via-sentinel-resource")
    String fallbackViaSentinelResource();

    @GetMapping(value = "fallback-via-common-fallback")
    String fallbackViaCommonFallback();

    @GetMapping(value = "rest-template-limit")
    String restTemplateLimit();

    @GetMapping(value = "rest-template-fallback")
    String restTemplateFallback();
}
