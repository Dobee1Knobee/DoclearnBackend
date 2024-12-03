package com.doclearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doclearn.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

	Optional<Author> findByEmail(String string);
	
	
}