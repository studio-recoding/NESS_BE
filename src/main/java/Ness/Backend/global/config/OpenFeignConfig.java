package Ness.Backend.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"Ness.Backend"})
public class OpenFeignConfig {
    /*
    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }
     */
}