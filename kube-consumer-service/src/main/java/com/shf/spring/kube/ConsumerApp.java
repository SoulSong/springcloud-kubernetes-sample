package com.shf.spring.kube;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer-service
 *
 * @author songhaifeng
 */
@SpringBootApplication
@EnableFeignClients
@Slf4j
public class ConsumerApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerApp.class).web(WebApplicationType.SERVLET).build().run(args);
    }

    @EventListener(value = ContextClosedEvent.class)
    public void releaseResources() {
        log.warn("Will shutdown, release all resources.");
    }

}
