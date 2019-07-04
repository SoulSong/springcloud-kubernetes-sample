package com.shf.spring.kube.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songhaifeng
 */
@RestController
@Slf4j
@RequestMapping("/resource")
public class ResourceController {

    @GetMapping(value = "no_need_permission")
    public String noNeedPermission() {
        return "no_need_permission";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping(value = "authority_with_user")
    public Object authorityWithUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreAuthorize("hasAuthority('reader')")
    @GetMapping(value = "authority_with_clientId")
    public Object authorityWithClientId() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreAuthorize("hasAuthority('writer')")
    @GetMapping(value = "authority_with_clientId2")
    public Object authorityWithClientId2() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping(value = "principal")
    public Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping(value = "roles")
    public Object getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @GetMapping(value = "get_extrainfo")
    public Map<String, Object> getExtraInfo() {
        return getExtraInfo(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * extract claims
     *
     * @param auth auth
     * @return map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, Object> getExtraInfo(Authentication auth) {
        final OAuth2AuthenticationDetails oauthDetails
                = (OAuth2AuthenticationDetails) auth.getDetails();
        return (Map<String, Object>) oauthDetails
                .getDecodedDetails();
    }

    /**
     * check token whether is expired
     *
     * @param request request
     * @return true means expired；otherwise is not expired.
     */
    @GetMapping(value = "is_expired")
    public boolean manualCheckToken(HttpServletRequest request) {
        final String auth = request.getHeader("Authorization");
        final String accessToken = auth.substring(7);
        final Jwt jwt = JwtHelper.decodeAndVerify(accessToken, new MacSigner("123"));
        try {
            final HashMap jwtMap = new ObjectMapper().readValue(jwt.getClaims(), HashMap.class);
            if (jwtMap.containsKey("user_name")) {
                log.info("PasswordCredential");
            } else if (jwtMap.containsKey("client_id")) {
                log.info("ClientCredential");
            }
            log.info(new Timestamp(new Long((Integer) jwtMap.get("exp")) * 1000).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return new Timestamp(new Long((Integer) jwtMap.get("exp")) * 1000).toLocalDateTime().isBefore(LocalDateTime.now(ZoneId.systemDefault()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return true;
    }

}



