package com.shf.spring.kube.feign.producer;

import com.shf.spring.kube.configuration.FeignHeaderInterceptor;
import com.shf.spring.kube.producer.service.HelloService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author songhaifeng
 */
@FeignClient(name = "${external.producer-service.name}", configuration = {FeignHeaderInterceptor.class})
public interface ProducerClient extends HelloService {
}
