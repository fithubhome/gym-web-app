package com.gym_app.api.config;

import com.gym_app.api.model.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public UserEntity user() {
        return new UserEntity();
    }
}
