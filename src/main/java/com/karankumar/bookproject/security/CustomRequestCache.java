package com.karankumar.bookproject.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

// From https://vaadin.com/learn/tutorials/modern-web-apps-with-spring-boot-and-vaadin/adding-a-login-screen-to-a-vaadin-app-with-spring-security
public class CustomRequestCache extends HttpSessionRequestCache {
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }
}
