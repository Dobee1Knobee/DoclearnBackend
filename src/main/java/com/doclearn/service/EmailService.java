package com.doclearn.service;

import com.doclearn.repository.AuthorRepository;
import com.doclearn.repository.TemporaryRegistRepository;
import com.doclearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemporaryRegistRepository temporaryRegistRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    @Autowired
    public EmailService(JavaMailSender mailSender, TemporaryRegistRepository temporaryRegistRepository, UserRepository userRepository, AuthorRepository authorRepository) {
        this.mailSender = mailSender;
        this.temporaryRegistRepository = temporaryRegistRepository;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }
    public void sendEmail(String to, String code) {
        validateEmailUniqueness(to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Код для валидации аккаунта DocLearn" );
        message.setText("Ваш код : " + code);
        message.setFrom("no-reply@doclearn.ru");
        mailSender.send(message);
    }
    private void validateEmailUniqueness(String email) {
        if (authorRepository.findByEmail(email).isPresent() | userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
