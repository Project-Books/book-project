package com.karankumar.bookproject.backend.service;

import javax.security.sasl.AuthenticationException;

public class InvalidOldPasswordException extends AuthenticationException {
    public InvalidOldPasswordException(String s) {

    }
}
