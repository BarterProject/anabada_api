package com.anabada.anabada_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // /** -> 와일드 카드
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE");
    }
}
