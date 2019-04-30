package com.shf.spring.kube.producer.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author songhaifeng
 */
@RequestMapping(value = "/call")
public interface HelloService {

    @GetMapping(value = "{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String hello(@PathVariable String name);
}
