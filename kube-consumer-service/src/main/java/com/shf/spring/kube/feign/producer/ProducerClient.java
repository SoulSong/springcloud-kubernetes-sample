package com.shf.spring.kube.feign.producer;

import com.shf.spring.kube.producer.endpoint.HelloEndpoint;
import com.shf.spring.kube.feign.FeignHeaderInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * producer-service client binder
 *
 * @author songhaifeng
 */
@FeignClient(name = "${external.producer-service.name}", url = "${external.producer-service.address}", configuration = {FeignHeaderInterceptor.class})
public interface ProducerClient extends HelloEndpoint {
}
