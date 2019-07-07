package com.shf.spring.kube.common.oauth2.token;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/8 23:48
 */
public final class TokenClaims {

    private String aud;
    private String clientId;
    private String userName;
    private boolean isExpired;
    private List<String> scopes;
    private List<String> authorities;
    private Map<String, Object> additionalInformation;
    private CredentialTypeEnum credentialType;

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public CredentialTypeEnum getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(CredentialTypeEnum credentialType) {
        this.credentialType = credentialType;
    }

    public enum CredentialTypeEnum {
        /**
         * password
         */
        PASSWORD,
        /**
         * clientId
         */
        CLIENTID;
    }

}
