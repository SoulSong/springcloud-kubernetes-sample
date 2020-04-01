package com.shf.spring.kube.consumer.endpoint;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author songhaifeng
 */
@RequestMapping(value = "/hello")
public interface HelloEndpoint {

    @GetMapping(value = "{name}")
    String hello(@PathVariable String name);

    @GetMapping(value = "call/producer-service", consumes = "application/json-v1")
    String call();

    /**
     * Test cpu limitation in k8s.
     *
     * @return number of cpu processor.
     */
    @GetMapping(value = "processor/number")
    int getProcessorNumber();
}
