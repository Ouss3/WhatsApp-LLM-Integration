package com.ouss.productservice.config;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "product.params")
@Data @AllArgsConstructor @NoArgsConstructor@ToString@Builder
public class ProductConfig {
    private int a,b;
}
