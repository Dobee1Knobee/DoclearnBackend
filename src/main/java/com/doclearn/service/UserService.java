package com.doclearn.service;

import java.util.List;
import java.util.Optional;

import com.doclearn.config.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doclearn.model.User;
import com.doclearn.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {


	private UserRepository userRepo;
	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
@Autowired
	public void setUserRepo(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	/**
	 * Сохраняет нового пользователя в базе данных, если email уникален.
	 * @param user - объект пользователя для сохранения.
	 * @return сохранённый пользователь.
	 */
	public User saveUser(User user) {
		validateEmailUniqueness(user.getEmail());
		return userRepo.save(user);
	}


	/**
	 * Получает всех пользователей.
	 * @return список всех пользователей.
	 */
	public List<User> getAll() {
		return userRepo.findAll();
	}

	/**
	 * Получает пользователя по ID.
	 * @param id - идентификатор пользователя.
	 * @return Optional с пользователем, если найден.
	 */
	public Optional<User> getUserByID(Long id) {
		return userRepo.findById(id);
	}

	/**
	 * Ищет пользователя по email.
	 * @param email - email пользователя.
	 * @return Optional с пользователем, если найден.
	 */
	public Optional<User> findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	/**
	 * Проверяет, существует ли пользователь с данным email. Если да, выбрасывает исключение.
	 * @param email - email для проверки уникальности.
	 * @throws IllegalArgumentException если пользователь с таким email уже существует.
	 */
	private void validateEmailUniqueness(String email) {
		if (userRepo.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь с email %s не найден", email)));

		return UserDetailsImpl.build(user);
	}
}
