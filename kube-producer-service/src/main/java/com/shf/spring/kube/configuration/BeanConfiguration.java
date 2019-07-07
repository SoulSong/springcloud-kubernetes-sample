package com.shf.spring.kube.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.common.oauth2.filter.TokenClaimsFilter;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;

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
}
