package com.doclearn.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Integer Id;
	
	
	@Getter @Setter
	@Column(nullable = false,unique = true)
	private String name;
	
	@OneToMany(mappedBy = "category")
	private Set<Course> courses = new HashSet<>();

	
	public Category(String name) {
		this.name = name;
	}


	public Category() {
	}
	
	
	
}
