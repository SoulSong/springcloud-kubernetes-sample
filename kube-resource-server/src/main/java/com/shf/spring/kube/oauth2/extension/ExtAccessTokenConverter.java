package com.shf.spring.kube.oauth2.extension;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description:
 * Create ExtAccessTokenConverter and set Authentication details with access token claims.
 * <p>
 * Note: DefaultAccessTokenConverter used to set Authentication details to Null.
 * </P>
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 14:38
 */
@Component
public class ExtAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        final OAuth2Authentication authentication
                = super.extractAuthentication(claims);
        authentication.setDetails(claims);
        return authentication;
    }
}
