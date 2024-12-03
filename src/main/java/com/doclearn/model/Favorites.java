package com.doclearn.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorites")
public class Favorites {
		
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "course_id",nullable = false)
	private Course course;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Favorites favorites = (Favorites) o;
		return Objects.equals(user, favorites.user) && Objects.equals(course, favorites.course);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, course);
	}

	public Favorites(User user, Course course) {
        this.user = user;
        this.course = course;
    }

	public Favorites() {

	}
}
