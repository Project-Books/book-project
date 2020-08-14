package com.karankumar.bookproject.backend.service;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyRegisteredException extends AuthenticationException {
    public UserAlreadyRegisteredException(final String msg) {
        super(msg);
    }
}