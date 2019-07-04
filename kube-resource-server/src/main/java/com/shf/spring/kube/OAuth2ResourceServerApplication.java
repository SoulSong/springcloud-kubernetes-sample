package com.shf.spring.kube;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author songhaifeng
 */
@SpringBootApplication
public class OAuth2ResourceServerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(OAuth2ResourceServerApplication.class).web(WebApplicationType.SERVLET).build().run(args);
	}
}
