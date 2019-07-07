package com.shf.spring.kube.common.oauth2.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shf.spring.kube.common.oauth2.exception.OauthTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/8 23:48
 */

public final class JwtHelperExt extends org.springframework.security.jwt.JwtHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtHelperExt.class);

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String USERNAME_KEY = "user_name";
    private static final String EXP_KEY = "exp";
    private static final String SCOPE_KEY = "scope";
    private static final String AUTHORITIES_KEY = "authorities";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings({"unchecked"})
    public static TokenClaims getTokenClaims(ObjectMapper objectMapper, String token, SignatureVerifier verifier) {
        try {
            final Jwt jwt = decodeAndVerify(token, verifier);
            final Map<String, Object> claimsMap = objectMapper.readValue(jwt.getClaims(), HashMap.class);

            LOGGER.debug("\ntoken:{}\n exp:{}", token, new Timestamp(new Long((Integer) claimsMap.get(EXP_KEY)) * 1000).toLocalDateTime().format(DATE_TIME_FORMATTER));
            boolean isExpired = new Timestamp(new Long((Integer) claimsMap.get(EXP_KEY)) * 1000).toLocalDateTime()
                    .isBefore(LocalDateTime.now(ZoneId.systemDefault()));
            final TokenClaims tokenClaims = new TokenClaims();
            tokenClaims.setExpired(isExpired);
            if (claimsMap.containsKey(CLIENT_ID_KEY)) {
                tokenClaims.setClientId(claimsMap.get(CLIENT_ID_KEY).toString());
                tokenClaims.setCredentialType(TokenClaims.CredentialTypeEnum.CLIENTID);
            }
            if (claimsMap.containsKey(USERNAME_KEY)) {
                tokenClaims.setUserName(claimsMap.get(USERNAME_KEY).toString());
                tokenClaims.setCredentialType(TokenClaims.CredentialTypeEnum.PASSWORD);
            }
            if (null != claimsMap.get(SCOPE_KEY)) {
                tokenClaims.setScopes(((List<String>) claimsMap.get(SCOPE_KEY)));
            }
            if (null != claimsMap.get(AUTHORITIES_KEY)) {
                tokenClaims.setAuthorities(((List<String>) claimsMap.get(AUTHORITIES_KEY)));
            }
            final Map<String, Object> additionalInformation = new HashMap<>();
            claimsMap.keySet().stream().filter(key -> !key.equalsIgnoreCase(CLIENT_ID_KEY) && !key.equalsIgnoreCase(USERNAME_KEY)
                    && !key.equalsIgnoreCase(EXP_KEY) && !key.equalsIgnoreCase(SCOPE_KEY) && !key.equalsIgnoreCase(AUTHORITIES_KEY))
                    .forEach(key -> {
                        additionalInformation.put(key, claimsMap.get(key));
                    });
            tokenClaims.setAdditionalInformation(additionalInformation);
            return tokenClaims;
        } catch (Exception e) {
            throw new OauthTokenException(e.getMessage());
        }
    }

}
