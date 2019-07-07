package com.shf.spring.kube.common.swagger;

import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;

import java.util.Arrays;
import java.util.List;

import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_VALUE_PREFIX;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/9 10:31
 */
public class SwaggerHelper {

    public static List<Parameter> builderCommonOperationParameters() {
        return Arrays.asList(
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
                        .name("Authorization")
                        .description("accessToken")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .defaultValue(DEFAULT_AUTHENTICATION_VALUE_PREFIX)
                        .order(1)
                        .build()
        );
    }

    public static ApiInfo builderApiInfo(final String title, final String description) {
        return new ApiInfoBuilder().title(title)
                .description(description)
                .contact(new Contact("songhaifeng", "", "songhaifengshuaige@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
