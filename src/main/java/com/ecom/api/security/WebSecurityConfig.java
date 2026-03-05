package com.ecom.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

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
                .cors(cors -> {})
                .addFilterBefore(requestFilter, AuthorizationFilter.class)
                // We need to make sure our authentication filter is run before the http request filter is run.
                .authorizeHttpRequests(auth->
                        // Specific exclusions or rules.
                        auth.requestMatchers("/","/test","/product", "/auth/register",
                                        "/auth/login", "/auth/verify", "/auth/forgot",
                                        "/websocket","/websocket/**",
                                        "/auth/reset", "/error").permitAll()
                                // Everything else should be authenticated.
                                    .anyRequest().authenticated());
        return http.build();
    }
}
