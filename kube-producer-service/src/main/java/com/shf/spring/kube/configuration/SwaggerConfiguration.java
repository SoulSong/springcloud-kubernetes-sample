package com.shf.spring.kube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.shf.spring.kube.common.swagger.SwaggerHelper.builderApiInfo;

/**
 * Description:
 * Config swagger documentation description.
 *
 * @Author: songhaifeng
 * @Date: 2019/4/30 17:45
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shf.spring.kube"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(builderApiInfo("Producer-Service", "Producer-Service REST API"));
    }

}
