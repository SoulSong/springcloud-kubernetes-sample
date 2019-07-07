package com.shf.spring.kube.controller;

import com.shf.spring.kube.configuration.BeanConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/5/11 00:33
 */
@RestController
@RequestMapping(name = "/configs/")
public class ConfigController {
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${config.variable0}")
    private String variable0;

    @Autowired
    private BeanConfiguration.Variables variables;
    @Autowired
    private BeanConfiguration.ReloadProperties reloadProperties;

    @GetMapping(value = "/secret", produces = MediaType.TEXT_HTML_VALUE)
    public String getSecretConfig() {
        return mongoUri;
    }

    @GetMapping(value = "/single/config/variable", produces = MediaType.TEXT_HTML_VALUE)
    public String getSingleConfigVariable() {
        return variable0;
    }

    @GetMapping(value = "/batch/config/variables", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BeanConfiguration.Variables getBatchConfigVariable() {
        return variables;
    }

    @GetMapping(value = "/reload/config/properties", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BeanConfiguration.ReloadProperties getReloadConfigProperties() {
        return reloadProperties;
    }
}
