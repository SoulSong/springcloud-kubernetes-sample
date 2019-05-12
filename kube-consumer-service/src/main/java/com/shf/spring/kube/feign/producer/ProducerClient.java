package com.shf.spring.kube.feign.producer;

import com.shf.spring.kube.feign.FeignHeaderInterceptor;
import com.shf.spring.kube.producer.endpoint.HelloEndpoint;
import org.springframework.cloud.kubernetes.ribbon.KubernetesServerList;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * producer-service client binder, discovery serves by ribbon.
 * {@link KubernetesServerList}
 *
 * @author songhaifeng
 */
@FeignClient(name = "${external.producer-service.name}", configuration = {FeignHeaderInterceptor.class})
public interface ProducerClient extends HelloEndpoint {
}
