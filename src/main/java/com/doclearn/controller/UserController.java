package com.doclearn.controller;

import java.util.List;

import com.doclearn.model.Author;
import com.doclearn.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.doclearn.model.User;
import com.doclearn.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorService authorService;

	@GetMapping
	public List<User> getUsers() {
		return userService.getAll();
	}

//	@PostMapping
//	public ResponseEntity<User> createUser(@RequestBody User user) {
//		try {
//			User createdUser = userService.saveUser(user);
//			return ResponseEntity.ok(createdUser);
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.badRequest().body(null);
//		}
//	}
//
//	@PostMapping("/authors")
//	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
//		try {
//			Author createdAuthor = authorService.saveAuthor(author);
//			return ResponseEntity.ok(createdAuthor);
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.badRequest().body(null);
//		}
//	}

	@GetMapping("/authors")
	public List<Author> getAuthors() {
		return authorService.getAll();

	}
}
