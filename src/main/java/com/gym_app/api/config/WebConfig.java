package com.gym_app.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public Filter openSessionInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSqlTimeConverter());
    }
}
