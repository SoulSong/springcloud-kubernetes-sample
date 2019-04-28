package com.shf.spring.kube.consumer.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author songhaifeng
 */
public interface HelloService {

    @GetMapping(value = "hello/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String hello(@PathVariable String name);

    @GetMapping(value = "call/producer-service", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String call();

}
