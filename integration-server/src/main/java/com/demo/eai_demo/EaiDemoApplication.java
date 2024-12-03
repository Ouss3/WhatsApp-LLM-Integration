package com.demo.eai_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EaiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EaiDemoApplication.class, args);
    }

}
