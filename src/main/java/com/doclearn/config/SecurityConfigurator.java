package com.doclearn.config;


import com.doclearn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
@Configuration
@EnableWebSecurity
public class SecurityConfigurator {

    private UserService userService;
    private TokenFilter tokenFilter;


    @Autowired
    public SecurityConfigurator(UserService userService, TokenFilter tokenFilter) {
        this.userService = userService;
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())  // Современный способ отключения CSRF
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.applyPermitDefaultValues();  // Разрешаем все методы и заголовки по умолчанию
                    return config;
                }))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))  // Обработка ошибки аутентификации
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())  // Обработка ошибки доступа
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("api/validation/author").permitAll()// Разрешаем доступ для всех к /public/**
                        .requestMatchers("/auth/**").permitAll()    // Разрешаем доступ для всех к /auth/**
                        .requestMatchers("/secured/**").authenticated()
                        .requestMatchers("/course/**").authenticated()// Требует аутентификацию
                        .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
                )

                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
