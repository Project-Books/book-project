package com.karankumar.bookproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application.properties")
@PropertySource("email.properties")

public class EmailService
{
    public static void main(String[] args) {
        SpringApplication.run(EmailService.class, args);
    }
}
