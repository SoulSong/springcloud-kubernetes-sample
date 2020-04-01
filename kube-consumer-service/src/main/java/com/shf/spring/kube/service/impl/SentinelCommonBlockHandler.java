package com.shf.spring.kube.service.impl;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/3/21 22:41
 */
public class SentinelCommonBlockHandler {

    public static String commonExceptionHandler(BlockException ex) {
        ex.printStackTrace();
        return String.format("Oops, common error occurred. message:[%s].", ex.toString());
    }

}
