package com.demo.eai_demo.config;

/**
 * Project Name: eai_demo
 * File Name: te
 * Created by: DELL
 * Created on: 11/26/2024
 * Description:
 * <p>
 * te is a part of the eai_demo project.
 */

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
public class CamelRouteConfig {

    @Value("${whatsapp.api_url}")
    private String apiUrl;

    @Value("${whatsapp.access_token}")
    private String apiToken;
    @Value("${client.service.url}")
    private String clientServiceUrl;
    Logger logger = LoggerFactory.getLogger( CamelRouteConfig.class);

    private final CamelContext camelContext;

    public CamelRouteConfig(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void init() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                restConfiguration()
                        .component("servlet")
                        .bindingMode(RestBindingMode.json);
                JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
                jacksonDataFormat.setPrettyPrint(true);

                from("direct:sendWhatsAppMessage")
                        .setHeader("Authorization", constant("Bearer " + apiToken))
                        .setHeader("Content-Type", constant("application/json"))
                        .marshal(jacksonDataFormat)
                        .process(exchange -> {
                            logger.debug("Sending JSON: {}", exchange.getIn().getBody(String.class));
                        })
                        .to(apiUrl)
                        .process(exchange -> {
                            logger.debug("Response: {}", exchange.getIn().getBody(String.class));
                        });

                from("direct:getAllClients")
                        .toD(clientServiceUrl + "/clients?page=${header.page}&size=${header.size}")
                        .process(exchange -> {
                            // Log the response body for debugging
                            String responseBody = exchange.getIn().getBody(String.class);
                            logger.debug("Response Body: {}", responseBody);
                        })
                        .onException(HttpOperationFailedException.class)
                        .log("HTTP Error: ${exception.message}")
                        .handled(true)
                        .to("mock:error");  // Or any logging endpoint to capture the error
                from("direct:getClientById")
                        .toD(clientServiceUrl + "/clients/${header.id}");


                from("direct:updateClient")
                        .setHeader("Content-Type", constant("application/json"))
                        .marshal(jacksonDataFormat)
                        .toD(clientServiceUrl + "/clients/${header.id}?httpMethod=PUT");

                from("direct:saveClient")
                        .setHeader("Content-Type", constant("application/json"))
                        .marshal(jacksonDataFormat)
                        .to(clientServiceUrl + "/clients?httpMethod=POST")
                        .onException(HttpOperationFailedException.class)
                        .log("HTTP Error: ${exception.message}")
                        .handled(true)
                        .to("mock:error");  // Or any logging endpoint to capture the error


                from("direct:deleteClient")
                        .toD(clientServiceUrl + "/clients/${header.id}?httpMethod=DELETE")
                        .process(exchange -> {
                            // Log the response body for debugging
                            String responseBody = exchange.getIn().getBody(String.class);
                            logger.debug("Response Body: {}", responseBody);
                        })
                        .onException(HttpOperationFailedException.class)
                        .log("HTTP Error: ${exception.message}")
                        .handled(true)
                        .to("mock:error");

            }
        });
    }
}

