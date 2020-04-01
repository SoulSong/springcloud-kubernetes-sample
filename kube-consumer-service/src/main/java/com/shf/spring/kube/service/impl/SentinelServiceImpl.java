package com.shf.spring.kube.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.shf.spring.kube.feign.sentinel.SentinelMissClient;
import com.shf.spring.kube.service.SentinelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * description :
 * Test sentinel fallback ability.
 *
 * @author songhaifeng
 * @date 2020/3/21 20:56
 */
@Service
@Slf4j
public class SentinelServiceImpl implements SentinelService {
    @Autowired
    private SentinelMissClient sentinelMissClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("loadBalanceRestTemplate")
    private RestTemplate loadBalanceRestTemplate;

    @Override
    public String hello() {
        return "Hello world.";
    }

    /************************************ fallback handler ************************************/
    /**
     * Fallback via the fallback definition from @FeignClient.
     *
     * @return str
     */
    @Override
    public String fallbackViaFeignClient() {
        return sentinelMissClient.miss();
    }

    /**
     * Fallback via the fallback method definition in the current class.
     *
     * @return str
     */
    @Override
    @SentinelResource(value = "fallback_test", fallback = "fallbackHandler")
    public String fallbackViaSentinelResource() {
        throw new RuntimeException("fallbackViaSentinelResource");
    }

    /**
     * The argus must the same as origin_method, can add an additional {@link Throwable} in the end..
     * Must be public method.
     *
     * @param throwable throwable
     * @return str
     */
    public String fallbackHandler(Throwable throwable) {
        throwable.printStackTrace();
        return "fallback via SentinelResource's fallback setting.";
    }

    /**
     * Fallback via the fallback method definition in a outer class.
     * The fallback method {@link  SentinelCommonFallback#commonFallback} is a global handler.
     *
     * @return str
     */
    @Override
    @SentinelResource(value = "fallback_test", fallbackClass = SentinelCommonFallback.class, fallback = "commonFallback")
    public String fallbackViaCommonFallback() {
        throw new RuntimeException("fallbackViaCommonFallback");
    }

    /************************************ limitation(blockException) handler ************************************/

    /**
     * 限流规则中资源名称必须为`limit_test`，此时方可通过{@link #exceptionHandler(BlockException)}方法进行
     *
     * @return
     */
    @Override
    @SentinelResource(value = "limit_test", blockHandler = "exceptionHandler")
    public String limitHandler() {
        return "hello, limit normally";
    }

    /**
     * 在限流规则中，与上一个方法属于同一资源
     *
     * @return
     */
    @Override
    @SentinelResource(value = "limit_test", blockHandler = "commonExceptionHandler", blockHandlerClass = SentinelCommonBlockHandler.class)
    public String limitCommonHandler() {
        return "hello, limit normally";
    }

    /**
     * {@link BlockException} handles method. The argus must the same as origin_method, can add an additional {@link BlockException} in the end.
     * Must be public method.
     *
     * @param ex ex
     * @return str
     */
    public String exceptionHandler(BlockException ex) {
        ex.printStackTrace();
        return String.format("Oops, error occurred. message:[%s].", ex.toString());
    }

    /************************************ SentinelRestTemplate handler ************************************/

    /**
     * 限流规则中资源名称为`GET:http://localhost:8080/call/abc`
     *
     * @return String
     */
    @Override
    public String restTemplateLimit() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/call/abc", HttpMethod.GET, new HttpEntity<>(new HashMap<>()), String.class);
        return responseEntity.getBody();
    }

    /**
     *
     * @return
     */
    @Override
    public String restTemplateFallback() {
        try {
            ResponseEntity<String> responseEntity = loadBalanceRestTemplate.exchange("http://miss-client:8080/hello/abc", HttpMethod.GET, new HttpEntity<>(new HashMap<>()), String.class);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return "N/A";
    }

}
