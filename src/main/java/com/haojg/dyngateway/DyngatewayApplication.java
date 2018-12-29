package com.haojg.dyngateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class DyngatewayApplication {

    @Resource
    DynamicRouteService dynamicRouteService;

    @RequestMapping("/hello")
    public String hello(){
        RouteDefinition definition = new RouteDefinition();
        definition.setId("newRoutes");
        URI uri = UriComponentsBuilder.fromHttpUrl("http://www.jd.com").build().toUri();
        definition.setUri(uri);

        //定义第一个断言
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");

        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("pattern", "/jd");
        predicate.setArgs(predicateParams);

        definition.setPredicates(Arrays.asList(predicate));

        dynamicRouteService.add(definition);

        return "hello";
    }

    /**
     * spring cloud gateway 配置方式之一，启动主程序配置，还有一种是配置文件配置
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/qq/**")
                        .and()
                        .uri("http://www.qq.com/"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(DyngatewayApplication.class, args);
    }

}

