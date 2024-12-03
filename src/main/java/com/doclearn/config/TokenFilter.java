package com.doclearn.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private JwtCore jwtCore;
    private UserDetailsService userDetailsService;

    @Autowired
    public TokenFilter(JwtCore jwtCore, UserDetailsService userDetailsService) {
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken authentication = null;

        try {
            // Извлечение токена из заголовка Authorization
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                logger.debug("JWT token extracted: {}");
            } else {
                logger.warn("Authorization header not found or invalid format");
            }

            // Если токен найден
            if (jwt != null) {
                try {
                    username = jwtCore.getNameFromJwt(jwt); // Получаем имя пользователя из токена
                    logger.debug("Username extracted from JWT: {}");
                } catch (ExpiredJwtException e) {
                    logger.warn("JWT expired: {}");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                    return;  // Прерываем выполнение, если токен истек
                } catch (Exception e) {
                    logger.error("JWT parsing failed: {}");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                // Если имя пользователя найдено, аутентифицируем его
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = userDetailsService.loadUserByUsername(username); // Загружаем детали пользователя
                    authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication); // Устанавливаем аутентификацию в контексте безопасности
                    logger.debug("User {} authenticated successfully");
                }
            }
        } catch (Exception e) {
            logger.error("Error during token filtering: {}");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }

        // Передаем управление следующему фильтру в цепочке
        filterChain.doFilter(request, response);
    }
}

