package com.ouss.productservice;

import com.ouss.productservice.config.GlobalConfig;
import com.ouss.productservice.config.ProductConfig;
import com.ouss.productservice.entites.Product;
import com.ouss.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({GlobalConfig.class , ProductConfig.class})
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner start(ProductRepository productReposetry){
        return args -> {
            for (int i=0;i<10;i++){
                Product c = Product.builder().name("marque"+i).description("description"+i).price((double)(10*i)).quantity(i).build();
                productReposetry.save(c);
            }
        };
    }
}
