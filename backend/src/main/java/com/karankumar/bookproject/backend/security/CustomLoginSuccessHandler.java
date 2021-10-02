package com.karankumar.bookproject.backend.security;

import com.karankumar.bookproject.backend.model.account.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private DatabaseUserDetailsService dtbsUserDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("success");

        UserDetails userDetails =  (UserDetails) authentication.getPrincipal();
        User user = (User) dtbsUserDetailsService.loadUserByUsername(userDetails.getUsername());
        if (user.getFailedAttempts() > 0) {
            dtbsUserDetailsService.resetFailedAttempts(user.getEmail());
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
