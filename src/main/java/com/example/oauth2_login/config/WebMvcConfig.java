package com.example.oauth2_login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // cors 적용할 url 패턴
        registry.addMapping("/**")
                // 자원을 공유할 오리진 지정
                .allowedOriginPatterns("*")
                // 요청 허용 메서드
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                // 요청 허용 헤더
                .allowedHeaders("*")
                // 쿠키 사용
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
