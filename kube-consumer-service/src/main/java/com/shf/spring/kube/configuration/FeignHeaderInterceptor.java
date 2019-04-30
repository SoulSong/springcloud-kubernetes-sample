package com.shf.spring.kube.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Description: Pass the header information through into the downstream service
 *
 * @Author: songhaifeng
 * @Date: 2019/4/27 02:33
 */
@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        assert attributes != null;
        final HttpServletRequest request = attributes.getRequest();
        final Enumeration<String> headerNames = request.getHeaderNames();
        if (null != headerNames) {
            while (headerNames.hasMoreElements()) {
                final String name = headerNames.nextElement();
                final String values = request.getHeader(name);
                requestTemplate.header(name, values);
            }
        }
    }
}