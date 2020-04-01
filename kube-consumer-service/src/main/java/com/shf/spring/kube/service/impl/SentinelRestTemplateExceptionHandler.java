package com.shf.spring.kube.service.impl;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.shf.spring.kube.common.exception.SentinelRestTemplateDegradeException;
import com.shf.spring.kube.common.exception.SentinelRestTemplateFlowException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/22 1:08
 */
@Slf4j
public class SentinelRestTemplateExceptionHandler {

    /**
     * FlowException：限流触发的异常通常可以抛出全局自定义异常，并通过全局自定义异常处理返回响应；
     *
     * @param request
     * @param body
     * @param execution
     * @param exception
     * @return
     */
    public static ClientHttpResponse flowHandler(HttpRequest request,
                                                          byte[] body,
                                                          ClientHttpRequestExecution execution,
                                                          BlockException exception) throws IOException {
        if (exception instanceof FlowException) {
            throw new SentinelRestTemplateFlowException(exception.getMessage(), exception);
        }

        return execution.execute(request, body);
    }

    /**
     * SentinelRestTemplate通用的熔断降级处理
     *
     * @param request
     * @param body
     * @param execution
     * @param exception
     * @return
     */
    public static ClientHttpResponse fallbackHandler(HttpRequest request, byte[] body,
                                              ClientHttpRequestExecution execution,
                                              BlockException exception) throws IOException {
        if (exception instanceof DegradeException) {
            throw new SentinelRestTemplateDegradeException(exception.getMessage(), exception);
        }
        return execution.execute(request, body);
    }

}
