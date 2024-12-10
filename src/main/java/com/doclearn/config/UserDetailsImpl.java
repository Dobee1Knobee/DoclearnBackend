package com.doclearn.config;

import com.doclearn.model.Author;
import com.doclearn.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private boolean isEnabled;  // Нужно для логики включенности
    public UserDetailsImpl(Long id, String firstName, String lastName, String password, String email, boolean isEnabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password; // Должно быть хэшированным паролем
        this.email = email;       // Должен быть email
        this.isEnabled = isEnabled;
    }
    // Статический метод для создания UserDetailsImpl из User
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.isEnabled());  // Учитываем состояние включенности пользователя
    }

    public static UserDetailsImpl buildAuthor(Author author) {
        return new UserDetailsImpl(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getPassword(),
                author.getEmail(),
                author.isEnabled());  // Учитываем состояние включенности пользователя
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Пример: добавить роль пользователя, если она есть
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Можно добавить логику, если есть срок действия аккаунта
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Можно добавить логику для блокировки аккаунта
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Можно добавить логику, если учетные данные имеют срок действия
    }

    @Override
    public boolean isEnabled() {
        return true; // Использовать значение из поля isEnabled
    }


}
