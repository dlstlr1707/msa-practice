package com.example.weather.config;

import com.example.weather.config.security.CustomAuthenticationFailHandler;
import com.example.weather.config.security.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->web.ignoring()
                .requestMatchers(
                        "/static/**","/css/**","/js/**"
                );
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomAuthenticationSuccessHandler successHandler,
            CustomAuthenticationFailHandler failHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(
                                        new AntPathRequestMatcher("/join","POST"),
                                        new AntPathRequestMatcher("/weather","GET"),
                                        new AntPathRequestMatcher("/member/join","GET"),
                                        new AntPathRequestMatcher("/member/login","GET"),
                                        new AntPathRequestMatcher("/member/weather","GET")
                                ).permitAll()
                                .anyRequest().authenticated()
                );
        http
                .formLogin(
                        form -> form
                                .loginPage("/member/login")
                                .loginProcessingUrl("/login")
                                .successHandler(successHandler)
                                .failureHandler(failHandler)
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

