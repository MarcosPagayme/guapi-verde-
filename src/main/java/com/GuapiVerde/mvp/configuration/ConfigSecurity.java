package com.GuapiVerde.mvp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfigSecurity {
    @Bean
    public SecurityFilterChain filterSecurity(HttpSecurity http) throws Exception {
        http.csrf(crsf -> crsf.disable())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }
    
}
