package com.doclearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.doclearn.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
	Optional<User> findByEmail(String email);
	Boolean existsByEmail(String email);
}
