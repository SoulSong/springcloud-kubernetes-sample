package com.shf.spring.kube.oauth2.extension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Customized token enhancer, here is showing how to add more information into the accessToken's payload.
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 18:00
 */
public class ExtTokenEnhancer implements TokenEnhancer {
    private static final String ORGANIZATION_LABEL = "organization";

    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        // only work on password mode.
        if (authentication.getPrincipal() instanceof UserDetails) {
            final Map<String, Object> additionalInfo = new HashMap<>(1);
            // add organization info
            additionalInfo.put(ORGANIZATION_LABEL, ((JwtUserDetails) authentication.getPrincipal()).getUserEntity().getOrganization());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(
                    additionalInfo);
        }
        return accessToken;
    }
}
