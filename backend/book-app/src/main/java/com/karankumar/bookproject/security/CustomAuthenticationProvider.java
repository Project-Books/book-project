package com.karankumar.bookproject.security;

import com.karankumar.bookproject.model.account.User;
import com.karankumar.bookproject.service.UserService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    private final UserService userService;

    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> userOpt = userService.findUserByEmail(authentication.getName());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isLocked()) {
                userService.unlockWhenTimeExpired(user);
            }
        }

        return super.authenticate(authentication);
    }
}
