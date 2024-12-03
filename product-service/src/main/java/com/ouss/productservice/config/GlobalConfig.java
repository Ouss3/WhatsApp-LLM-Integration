package com.ouss.productservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "global.params")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GlobalConfig {
    private int p1, p2;
}
