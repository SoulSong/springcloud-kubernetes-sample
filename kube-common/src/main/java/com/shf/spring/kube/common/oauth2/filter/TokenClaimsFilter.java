package com.shf.spring.kube.common.oauth2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.common.oauth2.exception.OauthTokenException;
import com.shf.spring.kube.common.oauth2.token.JwtHelperExt;
import com.shf.spring.kube.common.oauth2.token.TokenClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_HEADER_KEY;
import static com.shf.spring.kube.common.oauth2.Constant.DEFAULT_AUTHENTICATION_VALUE_PREFIX;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/9 02:15
 */
public class TokenClaimsFilter extends OncePerRequestFilter {

    private ObjectMapper objectMapper;
    private SignerVerifier signerVerifier;

    public TokenClaimsFilter(ObjectMapper objectMapper, SignerVerifier signerVerifier) {
        this.objectMapper = objectMapper;
        this.signerVerifier = signerVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain
                                            filterChain) throws ServletException, IOException {
        try {
            final String auth = request.getHeader(DEFAULT_AUTHENTICATION_HEADER_KEY);
            if (!StringUtils.isEmpty(auth) && auth.startsWith(DEFAULT_AUTHENTICATION_VALUE_PREFIX)) {
                final String accessToken = auth.substring(DEFAULT_AUTHENTICATION_VALUE_PREFIX.length());
                final TokenClaims tokenClaims = JwtHelperExt.getTokenClaims(objectMapper, accessToken, signerVerifier);
                // dont check expire status, it is checked in gateway already.
                ClaimsContextHolder.setCurrentCliam(tokenClaims);
            }
            filterChain.doFilter(request, response);
        } catch (OauthTokenException e) {
            Map<String, Object> responseBody = new HashMap<>(2);
            responseBody.put("message", e.getMessage());
            responseBody.put("bizCode", e.getBizCode());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        } finally {
            ClaimsContextHolder.clear();
        }
    }

}
