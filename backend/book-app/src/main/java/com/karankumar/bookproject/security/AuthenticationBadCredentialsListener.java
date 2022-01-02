package com.karankumar.bookproject.security;

import com.karankumar.bookproject.model.account.User;
import com.karankumar.bookproject.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationBadCredentialsListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final UserService userService;

    public AuthenticationBadCredentialsListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String userEmail = event.getAuthentication().getName();
        Optional<User> userOpt = userService.findUserByEmail(userEmail);

        if (userOpt.isPresent()) {
            User user = userService.increaseFailAttempts(userOpt.get());

            if (user.getFailedAttempts() >= UserService.MAX_FAILED_ATTEMPTS) {
                userService.lock(user);
            }
        }
    }
}
