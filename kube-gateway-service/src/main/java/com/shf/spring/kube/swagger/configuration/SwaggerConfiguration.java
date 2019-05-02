package com.shf.spring.kube.swagger.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Combine swagger configuration of all downstream service
 *
 * @Author: songhaifeng
 * @Date: 2019/5/1 23:44
 */
@Configuration
public class SwaggerConfiguration {

    @Autowired
    private GatewayProperties gatewayProperties;

    @Autowired
    private RouteLocator routeLocator;

    private static final String API_URI = "/v2/api-docs";
    private static final String PATH_LABEL = "Path";

    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            List<String> routes = new ArrayList<>();
            // fetch all route in gateway definition
            routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
            // filter effective route nodes by route_id and path(in predicates)
            gatewayProperties.getRoutes().stream()
                    .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                    .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                            .filter(predicateDefinition -> PATH_LABEL.equalsIgnoreCase(predicateDefinition.getName()))
                            .forEach(predicateDefinition -> resources.add(createResource(routeDefinition.getId(),
                                    predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")))));
            return resources;
        };
    }

    private SwaggerResource createResource(String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(location);
        swaggerResource.setLocation("/" + location + API_URI);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
