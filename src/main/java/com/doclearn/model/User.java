package com.doclearn.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable, Supplier<String> {

    private static final long serialVersionUID = 4934304505561987347L;


    @Override
	public int hashCode() {
		return Objects.hash( birthDate, createdAt, email, favorites, firstName, id, lastName, password, role);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", role=" + role + ", email=" + email + ", birthDate=" + birthDate + ", createdAt=" + createdAt
				+ ", favorites=" + (favorites != null ? favorites.toString() : "[]")  ;
	}

	@Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column(name  = "first_name")
    private String firstName;

    @Getter @Setter
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password",nullable = false)
    @Getter
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    @Column(name = "role")
    private UserRole role = UserRole.student;


    @Getter @Setter
    @Column(name ="email", unique = true)
    private String email;

    @Column(name = "birth_date")
    @Getter @Setter
    private LocalDate birthDate;

    @Column(name = "created_at", updatable = false)
    @Getter
    private LocalDate createdAt;

    @Getter @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
        )
    private List<Course> favorites;



    public User(String firstName, String lastName, String password, String email, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.createdAt = LocalDate.now();
    }

    protected User() {
    }
//
//    public void setPassword(String password) {
//        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
//    }
//
//    public boolean checkPassword(String rawPassword) {
//        return BCrypt.checkpw(rawPassword, this.password);
//    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String get() {
        return "";
    }


    public boolean isEnabled() {
        return false;
    }
}