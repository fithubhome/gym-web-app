package com.gym_app.api.config;

import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfig {
    @Bean
    public UserEntity user() {
        return new UserEntity();
    }

    @Bean
    public Profile profile() {
        Profile profile = new Profile();
        profile.setId(UUID.randomUUID());
        return profile;
    }
}
