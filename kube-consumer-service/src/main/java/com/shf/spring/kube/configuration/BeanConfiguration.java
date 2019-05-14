package com.shf.spring.kube.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/4/15 13:06
 */
@Configuration
@EnableConfigurationProperties
public class BeanConfiguration {

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
}
