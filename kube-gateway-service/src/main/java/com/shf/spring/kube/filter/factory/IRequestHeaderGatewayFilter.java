package com.shf.spring.kube.filter.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/8 12:25
 */
public interface IRequestHeaderGatewayFilter {
    /**
     * Set default common whiteList for filter
     */
    List<String> DEFAULT_WHITE_LIST = Arrays.asList("api-docs", "swagger");

    /**
     * return whiteList
     *
     * @return list
     */
    List<String> getWhiteList();

    /**
     * adjust whether current request path is in the whiteList
     *
     * @param requestPath requestPath
     * @return true means in the white list；otherwise is false.
     */
    default boolean isInWhiteList(String requestPath) {
        return getWhiteList().stream().anyMatch(requestPath::contains);
    }

    /**
     * combine customized whiteList and default whiteList.
     *
     * @param customizedWhiteList customized whiteList
     * @return whiteList
     */
    default List<String> compositeWhiteList(List<String> customizedWhiteList) {
        if (null == customizedWhiteList) {
            return DEFAULT_WHITE_LIST;
        }
        final List<String> whiteList = new ArrayList<>(DEFAULT_WHITE_LIST.size() + customizedWhiteList.size());
        whiteList.addAll(customizedWhiteList);
        whiteList.addAll(DEFAULT_WHITE_LIST);
        return whiteList;
    }

}
