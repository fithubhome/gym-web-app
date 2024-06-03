package com.gym_app.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public Filter openSessionInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }
}
