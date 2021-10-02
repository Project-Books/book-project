package com.karankumar.bookproject.backend.security;

import com.karankumar.bookproject.backend.model.account.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private DatabaseUserDetailsService dtbsUserDetailsService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        logger.info(email);
        User user = (User) dtbsUserDetailsService.loadUserByUsername(email);
        logger.info(user);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                logger.info("TU SOM");
                if (user.getFailedAttempts() < dtbsUserDetailsService.MAX_FAILED_ATTEMPTS - 1) {
                    dtbsUserDetailsService.increaseFailedAttempts(user);
                } else {
                    dtbsUserDetailsService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (!user.isAccountNonLocked()) {
                if (dtbsUserDetailsService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }

        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}