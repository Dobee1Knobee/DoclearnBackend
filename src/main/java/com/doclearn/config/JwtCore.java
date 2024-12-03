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


        public String generateToken(Authentication authentification){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentification.getPrincipal();
        return Jwts.builder().setSubject((userDetails.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + lifetime))
                .signWith(SignatureAlgorithm.HS256,secret)
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
