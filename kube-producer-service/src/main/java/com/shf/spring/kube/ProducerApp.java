package com.shf.spring.kube;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author songhaifeng
 */
@SpringBootApplication
public class ProducerApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ProducerApp.class).web(WebApplicationType.SERVLET).build().run(args);
    }

}
