package com.shf.spring.kube.controller;

import com.shf.spring.kube.common.oauth2.filter.ClaimsContextHolder;
import com.shf.spring.kube.common.oauth2.token.TokenClaims;
import com.shf.spring.kube.consumer.endpoint.HelloEndpoint;
import com.shf.spring.kube.feign.producer.ProducerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songhaifeng
 */
@RestController
@Slf4j
public class HelloControllerImpl implements HelloEndpoint {

    @Autowired
    private ProducerClient producerClient;

    @Override
    public String hello(@PathVariable String name) {
        TokenClaims tokenClaims = ClaimsContextHolder.getCurrentCliam().get();
        final String message = String.format("hello %s", null == tokenClaims ? "anonymity" :
                null != tokenClaims.getUserName() ? tokenClaims.getUserName() : tokenClaims.getClientId());
        log.info(message);
        return message;
    }

    @Override
    public String call() {
        return String.format("Get response from producer-service : \"%s\"", producerClient.hello("anonymity"));
    }

    @Override
    public int getProcessorNumber() {
        return Runtime.getRuntime().availableProcessors();
    }
}
