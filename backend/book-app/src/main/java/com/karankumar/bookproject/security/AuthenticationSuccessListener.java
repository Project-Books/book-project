package com.karankumar.bookproject.security;

import com.karankumar.bookproject.model.account.User;
import com.karankumar.bookproject.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final UserService userService;

    public AuthenticationSuccessListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Optional<User> userOpt = userService.findUserByEmail(event.getAuthentication().getName());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getFailedAttempts() > 0) {
                userService.resetFailAttempts(user);
            }
        }
    }
}
