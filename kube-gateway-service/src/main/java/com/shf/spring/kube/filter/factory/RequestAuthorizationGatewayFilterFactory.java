package com.shf.spring.kube.filter.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.common.oauth2.token.JwtHelperExt;
import com.shf.spring.kube.common.oauth2.token.TokenClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_HEADER_KEY;
import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_VALUE_PREFIX;

/**
 * Description:
 * Mock check permission
 *
 * @Author: songhaifeng
 * @Date: 2019/7/8 01:01
 */
@Slf4j
public class RequestAuthorizationGatewayFilterFactory extends AbstractRequestOAuth2GatewayFilterFactory {
    private static final AtomicBoolean IS_LOAD = new AtomicBoolean(false);
    private List<String> whiteList;
    private ObjectMapper objectMapper;
    private SignerVerifier signerVerifier;

    public RequestAuthorizationGatewayFilterFactory(ObjectMapper objectMapper, SignerVerifier signerVerifier) {
        super();
        this.objectMapper = objectMapper;
        this.signerVerifier = signerVerifier;
    }

    @Override
    public GatewayFilter apply(Config config) {
        // load the customized white list once.
        if (IS_LOAD.compareAndSet(false, true)) {
            whiteList = compositeWhiteList(loadWhiteList(config));
        }
        return (exchange, chain) -> {
            // in whiteList, directly forward
            if (isInWhiteList(exchange.getRequest().getPath().toString())) {
                return chain.filter(exchange);
            }
            // TODO: can check with token together
            assert null != exchange.getRequest().getHeaders().get(DEFAULT_AUTHENTICATION_HEADER_KEY);
            final String accessToken = exchange.getRequest().getHeaders().get(DEFAULT_AUTHENTICATION_HEADER_KEY).get(0).substring(DEFAULT_AUTHENTICATION_VALUE_PREFIX.length());
            final TokenClaims tokenClaims = JwtHelperExt.getTokenClaims(objectMapper, accessToken, signerVerifier);
            log.info("Check {}'s permission, has authorities:{}. Request resource:{}[{}].", null != tokenClaims.getUserName() ? tokenClaims.getUserName() : tokenClaims.getClientId(),
                    tokenClaims.getAuthorities().toArray(new String[0]),
                    exchange.getRequest().getPath(),
                    exchange.getRequest().getMethodValue());
            return chain.filter(exchange);
        };
    }

    @Override
    public List<String> getWhiteList() {
        return this.whiteList;
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
}
