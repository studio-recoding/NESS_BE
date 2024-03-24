package Ness.Backend.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"Ness.Backend.global"})
public class OpenFeignConfig {

}