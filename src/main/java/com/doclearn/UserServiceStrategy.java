package com.doclearn;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserServiceStrategy{
 UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
