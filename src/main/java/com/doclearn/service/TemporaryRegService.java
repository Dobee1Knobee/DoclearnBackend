package com.doclearn.service;

import com.doclearn.model.TemporaryRegToDel;
import com.doclearn.repository.TemporaryRegistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemporaryRegService {
    private final List<TemporaryRegToDel> tempRegMap = new ArrayList<TemporaryRegToDel>();

    @Autowired
    private TemporaryRegistRepository temporaryRegistRepository;

    public TemporaryRegToDel saveTempPerson(TemporaryRegToDel reg) {
        validateEmailUniqueness(reg.getEmail());
        return temporaryRegistRepository.save(reg);
    }

    public Optional<TemporaryRegToDel> findByEmail(String email) {
        return temporaryRegistRepository.findByEmail(email);
    }

    /**
     * Получает всех авторов.
     * @return список всех авторов.
     */
    public List<TemporaryRegToDel> getAll() {
        return temporaryRegistRepository.findAll();
    }

    private void validateEmailUniqueness(String email) {
        if (temporaryRegistRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
