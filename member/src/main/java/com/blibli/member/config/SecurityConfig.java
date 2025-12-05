package com.blibli.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // OPTIONAL: This is for Enabling password at login page
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🔓 PUBLIC ENDPOINTS — allowed without token
                        .requestMatchers(
                                "/api/member/register",
                                "/api/member/login",
                                "/api/member/logout",
                                "/api/member/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/product/**"
                        ).permitAll()
                        .requestMatchers("/api/member/profile").authenticated()

                        // 🔐 everything else requires token
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}