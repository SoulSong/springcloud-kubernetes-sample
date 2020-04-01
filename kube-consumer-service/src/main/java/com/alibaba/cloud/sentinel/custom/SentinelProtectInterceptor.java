package com.alibaba.cloud.sentinel.custom;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

/**
 * description :
 * Interceptor using by SentinelRestTemplate.
 * Modify the origin source code for handle exception with {@link com.shf.spring.kube.service.impl.SentinelRestTemplateExceptionHandler}.
 * Already commit a pr, link : https://github.com/alibaba/spring-cloud-alibaba/pull/1291
 *
 * @author songhaifeng
 * @date 2020/3/25 22:30
 */
public class SentinelProtectInterceptor implements ClientHttpRequestInterceptor {

    private final SentinelRestTemplate sentinelRestTemplate;

    private final RestTemplate restTemplate;

    public SentinelProtectInterceptor(SentinelRestTemplate sentinelRestTemplate,
                                      RestTemplate restTemplate) {
        this.sentinelRestTemplate = sentinelRestTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        URI uri = request.getURI();
        String hostResource = request.getMethod().toString() + ":" + uri.getScheme()
                + "://" + uri.getHost()
                + (uri.getPort() == -1 ? "" : ":" + uri.getPort());
        String hostWithPathResource = hostResource + uri.getPath();
        boolean entryWithPath = true;
        if (hostResource.equals(hostWithPathResource)) {
            entryWithPath = false;
        }
        Method urlCleanerMethod = BlockClassRegistry.lookupUrlCleaner(
                sentinelRestTemplate.urlCleanerClass(),
                sentinelRestTemplate.urlCleaner());
        if (urlCleanerMethod != null) {
            hostWithPathResource = (String) methodInvoke(urlCleanerMethod,
                    hostWithPathResource);
        }

        Entry hostEntry = null, hostWithPathEntry = null;
        ClientHttpResponse response = null;
        try {
            hostEntry = SphU.entry(hostResource, EntryType.OUT);
            if (entryWithPath) {
                hostWithPathEntry = SphU.entry(hostWithPathResource, EntryType.OUT);
            }
            response = execution.execute(request, body);
            if (this.restTemplate.getErrorHandler().hasError(response)) {
                Tracer.trace(
                        new IllegalStateException("RestTemplate ErrorHandler has error"));
            }
        } catch (Throwable e) {
            if (!BlockException.isBlockException(e)) {
                Tracer.trace(e);
            } else {
                return handleBlockException(request, body, execution, (BlockException) e);
            }
        } finally {
            if (hostWithPathEntry != null) {
                hostWithPathEntry.exit();
            }
            if (hostEntry != null) {
                hostEntry.exit();
            }
        }
        return response;
    }

    private ClientHttpResponse handleBlockException(HttpRequest request, byte[] body,
                                                    ClientHttpRequestExecution execution, BlockException ex) {
        Object[] args = new Object[]{request, body, execution, ex};
        // handle degrade
        if (isDegradeFailure(ex)) {
            Method fallbackMethod = extractFallbackMethod(sentinelRestTemplate.fallback(),
                    sentinelRestTemplate.fallbackClass());
            if (fallbackMethod != null) {
                return (ClientHttpResponse) methodInvoke(fallbackMethod, args);
            } else {
                return new SentinelClientHttpResponse();
            }
        }
        // handle flow
        Method blockHandler = extractBlockHandlerMethod(
                sentinelRestTemplate.blockHandler(),
                sentinelRestTemplate.blockHandlerClass());
        if (blockHandler != null) {
            return (ClientHttpResponse) methodInvoke(blockHandler, args);
        } else {
            return new SentinelClientHttpResponse();
        }
    }

    private Object methodInvoke(Method method, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (null != e.getTargetException() && e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
            throw new RuntimeException(e);
        }
    }

    private Method extractFallbackMethod(String fallback, Class<?> fallbackClass) {
        return BlockClassRegistry.lookupFallback(fallbackClass, fallback);
    }

    private Method extractBlockHandlerMethod(String block, Class<?> blockClass) {
        return BlockClassRegistry.lookupBlockHandler(blockClass, block);
    }

    private boolean isDegradeFailure(BlockException ex) {
        return ex instanceof DegradeException;
    }

}
