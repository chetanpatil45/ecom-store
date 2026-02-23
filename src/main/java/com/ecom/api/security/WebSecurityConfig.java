package com.ecom.api.security;

import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final JWTRequestFilter requestFilter;

    public WebSecurityConfig(JWTRequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // TODO - Proper authentication
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .addFilterBefore(requestFilter, AuthorizationFilter.class)
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/test","/product", "/auth/register", "/auth/login", "/auth/verify").permitAll()
                                    .anyRequest()
                                    .authenticated());
        return http.build();
    }
}
