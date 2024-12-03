package com.doclearn.doclearn;
import static org.junit.jupiter.api.Assertions.*; // Импортируем все статические методы из Assertions

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.doclearn.model.User;
import com.doclearn.model.UserRole;

import java.time.LocalDate;

@SpringBootTest
class CreateUserTest {

	private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John", "Doe", "password123",  "john.doe123123@example.com",  LocalDate.now());
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe123123@example.com", user.getEmail());
    }


//    @Test
//    public void testPasswordHashing() {
//        user.setPassword("password123");
//        assertTrue(user.checkPassword("password123"));
//        assertFalse(user.checkPassword("wrongpassword"));
//    }

    @Test
    public void testGetAuthorities() {
        assertEquals(1, user.getAuthorities().size());
        assertEquals("ROLE_student", user.getAuthorities().iterator().next().getAuthority());
    }

}
