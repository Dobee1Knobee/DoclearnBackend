package com.doclearn.controller;

import com.doclearn.config.JwtCore;
import com.doclearn.model.User;
import com.doclearn.repository.UserRepository;
import com.doclearn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityControllerUser {

    private  UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private  AuthenticationManager authenticationManager;
    private  JwtCore jwtCore;
    private UserService userDetailsService;

    public SecurityControllerUser(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                  AuthenticationManager authenticationManager, JwtCore jwtCore, UserService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User user) {
        Authentication authentication = null;
        System.out.println(user.getPassword());
        System.out.println(userDetailsService.loadUserByUsername(user.getEmail()));
        System.out.println(passwordEncoder.encode(user.getPassword()));

        if(passwordEncoder.matches(user.getPassword(), userDetailsService.loadUserByUsername(user.getEmail()).getPassword()) ) {
            System.out.println("Все заебись!");
        }else{
            System.out.println("Все хуево!");

        }
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtCore.generateToken(authentication);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        // Проверяем, существует ли уже пользователь с таким email
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        // Валидация пароля (например, минимальная длина 8 символов)
        if (user.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long");
        }

        // Хэшируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            // Сохраняем нового пользователя в базе данных
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user: " + e.getMessage());
        }

        // Возвращаем успешный ответ с информацией о зарегистрированном пользователе (например, email)
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with email: " + user.getEmail());
    }

}
