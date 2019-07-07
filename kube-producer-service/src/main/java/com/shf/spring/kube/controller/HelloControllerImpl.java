package com.shf.spring.kube.controller;

import com.shf.spring.kube.common.oauth2.filter.ClaimsContextHolder;
import com.shf.spring.kube.common.oauth2.token.TokenClaims;
import com.shf.spring.kube.producer.endpoint.HelloEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_HEADER_KEY;

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
        try {
            final TokenClaims tokenClaims = ClaimsContextHolder.getCurrentCliam().get();
            final String currentUser = null == tokenClaims ? name :
                    null != tokenClaims.getUserName() ? tokenClaims.getUserName() : tokenClaims.getClientId();
            message = String.format("hello %s, I am %s(hostname):%s(port).Receive token: %s", currentUser,
                    InetAddress.getLocalHost().getHostName(), port,
                    null == request.getHeader(DEFAULT_AUTHENTICATION_HEADER_KEY) ? "none" : request.getHeader(DEFAULT_AUTHENTICATION_HEADER_KEY));
            log.info(message);
            return message;
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return "call fail.";
    }
}
