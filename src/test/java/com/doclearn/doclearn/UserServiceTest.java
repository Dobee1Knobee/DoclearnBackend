package com.doclearn.doclearn;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.doclearn.model.User;
import com.doclearn.model.UserRole;
import com.doclearn.repository.UserRepository;
import com.doclearn.service.UserService;

@SpringBootTest
class UserServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void cleanUp() {
		// Удаляем все записи после каждого теста, чтобы тесты не влияли друг на друга
		userRepository.deleteAll();
	}

	@Test
	public void testFindAll() {
		// Создаем пользователей для проверки
		User user1 = new User("Ivan", "Ivanov", "password1",  "ivanov@example.com", LocalDate.now());
		User user2 = new User("Petr", "Petrov", "password2", "petrov@example.com", LocalDate.now());
		userRepository.save(user1);
		userRepository.save(user2);

		// Получаем всех пользователей из базы
		List<User> foundUsers = userRepository.findAll();
		assertEquals(2, foundUsers.size(), "Должно быть найдено два пользователя");

		for (User user : foundUsers) {
			Hibernate.initialize(user.getFavorites());  // Инициализация коллекции

			logger.info(() -> "User: " + user);

			if (user.getFavorites() != null) {
				logger.info(() -> "Favorites: " + user.getFavorites());
			} else {
				logger.info(() -> "No favorites found for user: " + user.getEmail());
			}
		}
	}

	@Test
	public void testSaveUserWithDuplicateEmail() {
		// Создаем пользователя с уникальным email
		String uniqueEmail = "unique_" + System.currentTimeMillis() + "@example.com";
		User user1 = new User("Egor", "Biriukov", "ooo123p",  uniqueEmail,LocalDate.now());
		userService.saveUser(user1); // Сохраняем первого пользователя

		// Пытаемся создать второго пользователя с тем же email
		User user2 = new User("Ivan", "Petrov", "anotherpassword", uniqueEmail, LocalDate.now());

		// Ожидаем, что будет выброшено исключение IllegalArgumentException
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.saveUser(user2);
		});

		// Проверяем сообщение исключения
		assertEquals("Email already exists", exception.getMessage());
	}
	@Test
	public void testDeleteUserById() {
		// Создаем пользователя для удаления
		User user = new User("Egor", "Biriukov", "ooo123p",  "34534asdf2436@example.com",LocalDate.now());
		userRepository.save(user);

		// Удаляем пользователя по email
		Optional<User> toDelete = userRepository.findByEmail(user.getEmail());
		assertTrue(toDelete.isPresent(), "Пользователь должен быть найден перед удалением");

		userRepository.deleteById(toDelete.get().getId());
		assertFalse(userRepository.findById(toDelete.get().getId()).isPresent(), "Пользователь должен быть удален");
	}

	@Test
	public void testGetUserById() {
		// Создаем и сохраняем пользователя
		User user = new User("Egor", "Biriukov", "ooo123p",  "34534asdf2436@example.com", LocalDate.now());
		User saved = userRepository.save(user);

		// Получаем пользователя по ID
		Optional<User> foundUser = userRepository.findById(saved.getId());
		assertTrue(foundUser.isPresent(), "Пользователь должен быть найден по ID");
		assertEquals(saved, foundUser.get(), "Найденный пользователь должен совпадать с сохраненным");
	}

	@Test
	public void testSaveUser() {
		// Создаем пользователя
		User user = new User("Egor", "Biriukov", "ooo123p", "3334233dfa4534asdf2436@example.com", LocalDate.now());



		// Сохраняем пользователя
		User savedUser = userRepository.save(user);

		// Проверяем, что пользователь сохранен корректно
		Optional<User> foundUser = userRepository.findById(savedUser.getId());
		assertTrue(foundUser.isPresent(), "Пользователь должен быть сохранен в базе данных");
		assertNotNull(savedUser.getId(), "ID сохраненного пользователя не должен быть null");
		assertEquals("Egor", savedUser.getFirstName(), "Имя пользователя должно совпадать");
	}
}
