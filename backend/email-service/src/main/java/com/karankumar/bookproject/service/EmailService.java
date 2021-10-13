package com.karankumar.bookproject.service;

import java.util.Map;

public interface EmailService {

    void sendSimpleMessage(String from, String to, String message);
}
