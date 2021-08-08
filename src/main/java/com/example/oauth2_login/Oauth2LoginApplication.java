package com.example.oauth2_login;

import com.example.oauth2_login.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Oauth2LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2LoginApplication.class, args);
    }

}
