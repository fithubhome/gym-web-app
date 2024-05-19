package com.gym_app.api.config;

import com.gym_app.api.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public User user() {
        return new User();
    }
}
