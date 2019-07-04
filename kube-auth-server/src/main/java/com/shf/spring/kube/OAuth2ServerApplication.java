package com.shf.spring.kube;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 00:17
 */
@SpringBootApplication
public class OAuth2ServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(OAuth2ServerApplication.class).web(WebApplicationType.SERVLET).build().run(args);
    }
}
