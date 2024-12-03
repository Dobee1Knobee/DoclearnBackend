package com.doclearn.controller;

import com.doclearn.model.TemporaryRegToDel;
import com.doclearn.service.EmailService;
import com.doclearn.service.TemporaryRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/temp-reg")
public class TemporaryRegController {

    @Autowired
    private TemporaryRegService temporaryRegService;

    @Autowired
    private EmailService emailService;

    // Хранилище для временных регистраций
    private Map<String, TemporaryRegToDel> temporaryRegToDelMap = new HashMap<>();

    // Генерация случайного кода
    private String generateCode() {
        return String.format("%05d", new Random().nextInt(100000));
    }

    // Создание временной регистрации
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody TemporaryRegToDel reg) {
        String generatedCode = generateCode();
        emailService.sendEmail(reg.getEmail(), generatedCode);
        temporaryRegToDelMap.put(generatedCode, reg);
        return ResponseEntity.ok("Регистрация прошла успешно, код отправлен на почту.");
    }

    // Валидация кода
    @PostMapping("/validation")
    public ResponseEntity<String> validate(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        TemporaryRegToDel tempReg = temporaryRegToDelMap.get(code);

        if (tempReg != null) {
            // Успешная валидация, здесь можно добавить логику сохранения пользователя в базу данных

            temporaryRegService.saveTempPerson(tempReg);
            temporaryRegToDelMap.remove(code); // Удаляем запись после успешной валидации
            return ResponseEntity.ok("Валидация прошла успешно.");
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
