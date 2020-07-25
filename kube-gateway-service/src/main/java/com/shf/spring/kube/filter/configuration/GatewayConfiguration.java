package com.shf.spring.kube.filter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.filter.factory.RequestAuthenticationGatewayFilterFactory;
import com.shf.spring.kube.filter.factory.RequestAuthorizationGatewayFilterFactory;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;

import java.util.List;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/6/13 16:14
 */
@Configuration
public class GatewayConfiguration {
    private static final String REDIS_RATE_LIMITER_SCRIPT = "new_redisRequestRateLimiterScript";

    @Autowired
    private ObjectMapper objectMapper;

    /************************replace default script(tokenbucket_request_rate_limiter.lua)******************************/
    @Bean(REDIS_RATE_LIMITER_SCRIPT)
    @SuppressWarnings("unchecked")
    public RedisScript redisRequestRateLimiterScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/tokenbucket_request_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    @Bean
    public RedisRateLimiter redisRateLimiter(ReactiveStringRedisTemplate redisTemplate,
                                             @Qualifier(REDIS_RATE_LIMITER_SCRIPT) RedisScript<List<Long>> redisScript,
                                             ConfigurationService configurationService) {
        return new RedisRateLimiter(redisTemplate, redisScript, configurationService);
    }

    @Bean
    public RequestAuthenticationGatewayFilterFactory authenticationGatewayFilterFactory() {
        return new RequestAuthenticationGatewayFilterFactory(objectMapper, signerVerifier());
    }

    @Bean
    public RequestAuthorizationGatewayFilterFactory requestAuthorizationGatewayFilterFactory() {
        return new RequestAuthorizationGatewayFilterFactory(objectMapper, signerVerifier());
    }

    @Builder
    public SignerVerifier signerVerifier() {
        return new MacSigner("123");
    }

}
