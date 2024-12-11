package com.doclearn.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {
    @Value("${doclearn.app.secret}")
    private String secret;
    @Value("${doclearn.app.expirationMs}")
    private int lifetime;


    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // Используем username как subject
                .setIssuedAt(new Date())  // Время создания токена
                .setExpiration(new Date(System.currentTimeMillis() + lifetime))  // Время истечения токена
                .signWith(SignatureAlgorithm.HS256, secret)  // Используем секретный ключ для подписи
                .compact();
    }

    public String getNameFromJwt(String jwt) {
        // Строим JwtParser через JwtParserBuilder
        JwtParser parser = Jwts.parser()
                .setSigningKey(secret)  // Устанавливаем секретный ключ для подписи
                .build();  // Создаем JwtParser

        // Парсим JWT и извлекаем тело (Claims)
        Claims claims = parser.parseClaimsJws(jwt).getBody();

        // Возвращаем subject (например, имя или идентификатор пользователя)
        return claims.getSubject();
    }


}
