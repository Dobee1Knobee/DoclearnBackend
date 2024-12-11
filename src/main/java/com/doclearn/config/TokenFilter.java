package com.doclearn.config;

import com.doclearn.service.AuthorService;
import com.doclearn.service.CustomDetailsService;
import com.doclearn.service.UserService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JwtCore jwtCore;
    private final UserService userService;
    private final AuthorService authorService;
    private  CustomDetailsService customDetailsService;

    @Autowired
    public TokenFilter(JwtCore jwtCore, CustomDetailsService customDetailsService, UserService userService, AuthorService authorService) {
        this.jwtCore = jwtCore;
        this.customDetailsService = customDetailsService;
        this.userService = userService;
        this.authorService = authorService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                logger.debug("JWT token extracted: {}");
            } else {
                logger.warn("Authorization header not found or invalid format");
            }

            if (jwt != null) {
                try {
                    username = jwtCore.getNameFromJwt(jwt);
                    logger.debug("Username extracted from JWT: {}");
                } catch (ExpiredJwtException e) {
                    logger.warn("JWT expired: {}");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                    return;
                } catch (Exception e) {
                    logger.error("JWT parsing failed: {}");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        // Пытаемся сначала загрузить через UserService
                        userDetails = userService.loadUserByUsername(username);
                    } catch (UsernameNotFoundException e) {
                        // Если пользователь не найден через UserService, пробуем загрузить через AuthorService
                        userDetails = authorService.loadUserByUsername(username);
                    }
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("User {} authenticated successfully");
                    } else {
                        logger.warn("User details not found for username: {}");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error during token filtering: {}",e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }
}