package com.shf.spring.kube.filter.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/8 13:51
 */
public abstract class AbstractRequestOAuth2GatewayFilterFactory
        extends AbstractGatewayFilterFactory<AbstractRequestOAuth2GatewayFilterFactory.Config>
        implements IRequestHeaderGatewayFilter {

    public AbstractRequestOAuth2GatewayFilterFactory() {
        super(Config.class);
    }

    protected List<String> loadWhiteList(Config config) {
        return null == config.whiteList ? Lists.newArrayList() : Stream.of(config.whiteList.split(","))
                .map(StringUtils::trimToEmpty).filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

    public Mono<Void> setResponse(ServerHttpResponse response, HttpStatus httpStatus, String message) {
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        if (StringUtils.isNotBlank(message)) {
            try {
                final Map<String, Object> result = new HashMap<>(1);
                result.put("message", message);
                String value = getObjectMapper().writeValueAsString(result);
                byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().allocateBuffer(bytes.length).write(bytes);
                return response.writeAndFlushWith(Mono.just(Mono.just(buffer)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return response.writeAndFlushWith(Mono.empty());
    }

    protected abstract ObjectMapper getObjectMapper();

    public static class Config {

        protected String whiteList;

        public Config setWhiteList(String whiteList) {
            this.whiteList = whiteList;
            return this;
        }

    }

}
