package com.shf.spring.kube.filter.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/6/13 16:14
 */
@Configuration
public class GatewayRateLimiterConfiguration {
    private static final String REDIS_RATE_LIMITER_SCRIPT="new_redisRequestRateLimiterScript";

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
    public RedisRateLimiter redisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate,
                                             @Qualifier(REDIS_RATE_LIMITER_SCRIPT) RedisScript<List<Long>> redisScript,
                                             Validator validator) {
        return new RedisRateLimiter(redisTemplate, redisScript, validator);
    }



}
