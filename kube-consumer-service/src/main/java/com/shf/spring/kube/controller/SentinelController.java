package com.shf.spring.kube.controller;

import com.shf.spring.kube.consumer.endpoint.SentinelEndpoint;
import com.shf.spring.kube.service.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/21 16:35
 */
@RestController
public class SentinelController implements SentinelEndpoint {

    @Autowired
    private SentinelService sentinelService;

    @Override
    public String hello() {
        return sentinelService.hello();
    }

    @Override
    public String limitHandler() {
        return sentinelService.limitHandler();
    }

    @Override
    public String limitCommonHandler() {
        return sentinelService.limitCommonHandler();
    }

    @Override
    public String fallbackViaFeignClient() {
        return sentinelService.fallbackViaFeignClient();
    }

    @Override
    public String fallbackViaSentinelResource() {
        return sentinelService.fallbackViaSentinelResource();
    }

    @Override
    public String fallbackViaCommonFallback() {
        return sentinelService.fallbackViaCommonFallback();
    }

    @Override
    public String restTemplateLimit() {
        return sentinelService.restTemplateLimit();
    }

    @Override
    public String restTemplateFallback() {
        return sentinelService.restTemplateFallback();
    }

}
