package com.shf.spring.kube.filter.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.common.oauth2.token.JwtHelperExt;
import com.shf.spring.kube.common.oauth2.token.TokenClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_HEADER_KEY;
import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_VALUE_PREFIX;

/**
 * Description:
 * Check accessToken
 * <p>
 * {@link RequestAuthenticationGatewayFilterFactory} runs before {@link RequestAuthorizationGatewayFilterFactory}
 *
 * @Author: songhaifeng
 * @Date: 2019/7/8 01:01
 */
@Slf4j
public class RequestAuthenticationGatewayFilterFactory extends AbstractRequestOAuth2GatewayFilterFactory {
    private static final AtomicBoolean IS_LOAD = new AtomicBoolean(false);
    private List<String> whiteList;
    private ObjectMapper objectMapper;
    private SignerVerifier signerVerifier;

    public RequestAuthenticationGatewayFilterFactory(ObjectMapper objectMapper, SignerVerifier signerVerifier) {
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

            // get jwt token from header
            ServerHttpResponse response = exchange.getResponse();
            final List<String> auths = exchange.getRequest().getHeaders().get(DEFAULT_AUTHENTICATION_HEADER_KEY);
            if (null == auths) {
                return setResponse(response, HttpStatus.UNAUTHORIZED, "Miss accessToken in header.");
            }

            final String auth = auths.get(0);
            if (!auth.startsWith(DEFAULT_AUTHENTICATION_VALUE_PREFIX)) {
                return setResponse(response, HttpStatus.UNAUTHORIZED, "Invalid accessToken.");
            }

            // check token
            try {
                final String accessToken = auth.substring(DEFAULT_AUTHENTICATION_VALUE_PREFIX.length());
                final TokenClaims tokenClaims = JwtHelperExt.getTokenClaims(objectMapper, accessToken, signerVerifier);
                // invalid
                if (tokenClaims.isExpired()) {
                    return setResponse(response, HttpStatus.UNAUTHORIZED, "AccessToken is expired.");
                }

                // valid
                return chain.filter(exchange);
            } catch (Exception e) {
                return setResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
            }
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
