package com.shf.spring.kube.configuration;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.common.exception.handler.SentinelRestTemplateExceptionHandler;
import com.shf.spring.kube.common.oauth2.filter.TokenClaimsFilter;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;
import org.springframework.web.client.RestTemplate;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/4/15 13:06
 */
@Configuration
@EnableConfigurationProperties
public class BeanConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @ConfigurationProperties(prefix = "configs")
    public Variables variables() {
        return new Variables();
    }

    @Bean
    @ConfigurationProperties(prefix = "configs.reload")
    public ReloadProperties reloadProperties() {
        return new ReloadProperties();
    }

    @Data
    public class Variables {
        private String variable1;
        private String variable2;
    }

    @Data
    public class ReloadProperties {
        private String property1;
        private String property2;
    }

    @Builder
    public SignerVerifier signerVerifier() {
        return new MacSigner("123");
    }

    @Bean
    public FilterRegistrationBean tokenClaimsFilter() {
        FilterRegistrationBean<TokenClaimsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenClaimsFilter(objectMapper, signerVerifier()));
        return registrationBean;
    }

    @Bean
    public SentinelRestTemplateExceptionHandler globalExceptionHandler(){
        return new SentinelRestTemplateExceptionHandler();
    }

    @Bean
    @SentinelRestTemplate(blockHandler = "flowHandler",
            blockHandlerClass = com.shf.spring.kube.service.impl.SentinelRestTemplateExceptionHandler.class,
            fallback = "fallbackHandler",
            fallbackClass = com.shf.spring.kube.service.impl.SentinelRestTemplateExceptionHandler.class)
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean("loadBalanceRestTemplate")
    @LoadBalanced
    @SentinelRestTemplate(blockHandler = "flowHandler",
            blockHandlerClass = com.shf.spring.kube.service.impl.SentinelRestTemplateExceptionHandler.class,
            fallback = "fallbackHandler",
            fallbackClass = com.shf.spring.kube.service.impl.SentinelRestTemplateExceptionHandler.class)
    public RestTemplate loadBalanceRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
