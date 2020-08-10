package com.karankumar.bookproject.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toUnmodifiableList;

@Component
public class UserDetailsMapper {
    public User toUserDetails(com.karankumar.bookproject.backend.entity.User user) {
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole()))
                    .collect(toUnmodifiableList())
        );
    }
}
