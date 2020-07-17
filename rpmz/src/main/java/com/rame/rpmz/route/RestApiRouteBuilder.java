package com.rame.rpmz.route;

import com.rame.rpmz.service.OrderService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestApiRouteBuilder extends RouteBuilder {

    // @Autowired
    // private OrderService orderService;

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("servlet")
            .port(8091)
            .host("localhost")
            .bindingMode(RestBindingMode.json);

        rest()
            .get("/hello")
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .route()
            .setBody(constant("Hello world! "));

        // rest()
        //     .post("/new-order")
        //     .produces(MediaType.APPLICATION_JSON_VALUE)
        //     .route()
        //     .setBody(()->));

        rest()
            .get("/orders")
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .route()
            .to("bean:orderService?method=getOrders");

        from("rest:get:hello:/french/{me}") .transform().simple("Bonjour ${header.me}");
    }
    
}