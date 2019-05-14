package com.shf.spring.kube.controller;

import com.shf.spring.kube.configuration.BeanConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @GetMapping("/secret")
    public String getSecretConfig() {
        return mongoUri;
    }

    @GetMapping("/single/config/variable")
    public String getSingleConfigVariable() {
        return variable0;
    }

    @GetMapping("/batch/config/variables")
    public BeanConfiguration.Variables getBatchConfigVariable() {
        return variables;
    }

    @GetMapping("/reload/config/properties")
    public BeanConfiguration.ReloadProperties getReloadConfigProperties() {
        return reloadProperties;
    }
}
