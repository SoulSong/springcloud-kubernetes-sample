package com.shf.spring.kube.service;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/21 20:54
 */
public interface SentinelService {

    String hello();

    String fallbackViaFeignClient();

    String fallbackViaSentinelResource();

    String fallbackViaCommonFallback();

    String limitHandler();

    String limitCommonHandler();

    String restTemplateLimit();

    String restTemplateFallback();

}
