package com.shf.spring.kube.controller;

import com.shf.spring.kube.producer.endpoint.HelloEndpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author songhaifeng
 */
@RestController
@Slf4j
public class HelloControllerImpl implements HelloEndpoint {

    @Autowired
    private HttpServletRequest request;

    @Value("${server.port:8080}")
    private String port;

    @Override
    public String hello(@PathVariable String name) {
        final String message;
        final String tokenHeader = "token";
        try {
            message = String.format("hello %s, I am %s(hostname):%s(port).Receive token: %s", name,
                    InetAddress.getLocalHost().getHostName(), port,
                    null == request.getHeader(tokenHeader) ? "none" : request.getHeader(tokenHeader));
            log.info(message);
            return message;
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return "call fail.";
    }
}
