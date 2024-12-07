package com.doclearn.controller;

import com.doclearn.config.JwtCore;
import com.doclearn.model.Author;
import com.doclearn.model.TemporaryRegToDel;
import com.doclearn.model.User;
import com.doclearn.repository.AuthorRepository;
import com.doclearn.repository.UserRepository;
import com.doclearn.service.EmailService;
import com.doclearn.service.TemporaryRegService;
import com.doclearn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class TemporaryRegController {

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TemporaryRegService temporaryRegService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;
    // Хранилище для временных регистраций
    private Map<String, User> temporaryRegToDelMap = new HashMap<>();
    private Map<String, Author> temporaryRegToDelMapAuthor = new HashMap<>();
    public TemporaryRegController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtCore jwtCore) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Генерация случайного кода
    private String generateCode() {
        return String.format("%05d", new Random().nextInt(100000));
    }

    // Создание временной регистрации
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User reg) {
        String generatedCode = generateCode();
        emailService.sendEmail(reg.getEmail(), generatedCode);
        temporaryRegToDelMap.put(generatedCode, reg);
        return ResponseEntity.ok("Регистрация прошла успешно, код отправлен на почту.");
    }
    @PostMapping("/register/author")
    public ResponseEntity<String> register(@RequestBody Author reg) {
        String generatedCode = generateCode();
        emailService.sendEmail(reg.getEmail(), generatedCode);
        temporaryRegToDelMapAuthor.put(generatedCode, reg);
        return ResponseEntity.ok("Регистрация прошла успешно, код отправлен на почту.");
    }

    // Валидация кода
    @PostMapping("/validation/author")
    public ResponseEntity<String> validateAuthor(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        Author tempReg = temporaryRegToDelMapAuthor.get(code);

        if (tempReg != null) {
            // Успешная валидация, здесь можно добавить логику сохранения пользователя в базу данных
            tempReg.setPassword(passwordEncoder.encode(tempReg.getPassword()));

            try {
                // Сохраняем нового пользователя в базе данных
                authorRepository.save(tempReg);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user: " + e.getMessage());
            }
            temporaryRegToDelMap.remove(code); // Удаляем запись после успешной валидации
            // Возвращаем успешный ответ с информацией о зарегистрированном пользователе (например, email)
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with email: " + tempReg.getEmail());


        } else {
            return ResponseEntity.badRequest().body("Неправильный код валидации.");
        }
    }
    // Валидация кода
    @PostMapping("/validation/user")
    public ResponseEntity<String> validate(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        User tempReg = temporaryRegToDelMap.get(code);

        if (tempReg != null) {
            // Успешная валидация, здесь можно добавить логику сохранения пользователя в базу данных
            tempReg.setPassword(passwordEncoder.encode(tempReg.getPassword()));

            try {
                // Сохраняем нового пользователя в базе данных
                userRepository.save(tempReg);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user: " + e.getMessage());
            }
            temporaryRegToDelMap.remove(code); // Удаляем запись после успешной валидации
            // Возвращаем успешный ответ с информацией о зарегистрированном пользователе (например, email)
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with email: " + tempReg.getEmail());


        } else {
            return ResponseEntity.badRequest().body("Неправильный код валидации.");
        }
    }

// Получение всех временных регистраций
    @GetMapping("/all")
    public List<TemporaryRegToDel> getAll() {
        return temporaryRegService.getAll();
    }

    // Получение информации о регистрации по email

    @GetMapping("/find")
    public TemporaryRegToDel getByEmail(@RequestParam String email) {
        return temporaryRegService.getAll().stream()
                .filter(reg -> reg.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
