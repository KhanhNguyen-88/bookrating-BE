package com.buihuuduy.book_rating.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Áp dụng cho tất cả endpoint
                        .allowedOrigins("http://localhost:63342") // Cho phép origin này
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các HTTP method được phép
                        .allowedHeaders("*") // Cho phép tất cả headers
                        .allowCredentials(true);
            }
        };
    }
}