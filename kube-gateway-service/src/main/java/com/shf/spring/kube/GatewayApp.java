package com.shf.spring.kube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * Gateway app
 *
 * @author songhaifeng
 */
@SpringBootApplication
@Configuration
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

    /**
     * Get userId from the request header as limit key
     *
     * @return
     */
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getOrDefault("userId", Collections.singletonList("anonymity")).get(0));
    }
}
