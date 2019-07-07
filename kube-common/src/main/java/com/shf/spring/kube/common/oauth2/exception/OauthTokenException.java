package com.shf.spring.kube.common.oauth2.exception;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/10 13:14
 */
public class OauthTokenException extends RuntimeException {

    private static final int DEFAULT_BIZ_CODE = 40001;
    private int bizCode;

    public OauthTokenException(String message) {
        this(DEFAULT_BIZ_CODE, message);
    }

    public OauthTokenException(int bizCode, String message) {
        super(message);
        this.bizCode = bizCode;
    }

    public int getBizCode() {
        return bizCode;
    }
}
