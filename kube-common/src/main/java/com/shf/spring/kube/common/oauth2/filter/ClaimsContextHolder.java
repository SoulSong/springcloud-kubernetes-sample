package com.shf.spring.kube.common.oauth2.filter;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.shf.spring.kube.common.oauth2.token.TokenClaims;

/**
 * Description:
 * Store tokenCliams
 *
 * @Author: songhaifeng
 * @Date: 2019/7/9 02:16
 */
public class ClaimsContextHolder {

    private static final TransmittableThreadLocal<TokenClaims> CURRENT_CLIAM = new TransmittableThreadLocal<>();

    public static TransmittableThreadLocal<TokenClaims> getCurrentCliam() {
        return CURRENT_CLIAM;
    }

    public static void setCurrentCliam(TokenClaims tokenClaims) {
        CURRENT_CLIAM.set(tokenClaims);
    }

    public static void clear() {
        CURRENT_CLIAM.remove();
    }
}
