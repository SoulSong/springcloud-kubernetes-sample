package com.shf.spring.kube.configuration;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

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
                // exclude feign-client
                .apis(Predicates.and(Predicates.not(RequestHandlerSelectors.basePackage("com.shf.spring.kube.feign")),
                        RequestHandlerSelectors.basePackage("com.shf.spring.kube")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(helloEndPointsInfo())
                .globalOperationParameters(Arrays.asList(
                        new ParameterBuilder()
                                .name("Content-type")
                                .description("Content-type")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .defaultValue(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .order(0)
                                .build(),
                        new ParameterBuilder()
                                .name("token")
                                .description("token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .order(1)
                                .build()
                ));
    }

    private ApiInfo helloEndPointsInfo() {
        return new ApiInfoBuilder().title("Consumer-Service REST API")
                .description("Consumer-Service")
                .contact(new Contact("songhaifeng", "", "songhaifengshuaige@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
