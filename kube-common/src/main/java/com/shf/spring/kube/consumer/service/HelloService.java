package com.shf.spring.kube.consumer.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author songhaifeng
 */
@RequestMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface HelloService {

    @GetMapping(value = "{name}")
    String hello(@PathVariable String name);

    @GetMapping(value = "call/producer-service", consumes = "application/json-v1")
    String call();

}
