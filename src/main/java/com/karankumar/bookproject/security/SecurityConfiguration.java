/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// From https://vaadin.com/learn/tutorials/modern-web-apps-with-spring-boot-and-vaadin/adding-a-login-screen-to-a-vaadin-app-with-spring-security
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private static final String REGISTRATION_URL = "/register";

    private final DatabaseUserDetailsService databaseUserDetailsService;
    private final DatabaseUserDetailsPasswordService databaseUserDetailsPasswordService;

    public SecurityConfiguration(DatabaseUserDetailsService databaseUserDetailsService,
                                 DatabaseUserDetailsPasswordService databaseUserDetailsPasswordService) {
        this.databaseUserDetailsService = databaseUserDetailsService;
        this.databaseUserDetailsPasswordService = databaseUserDetailsPasswordService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(databaseUserDetailsService)
            .userDetailsPasswordManager(databaseUserDetailsPasswordService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/frontend/**",
                "/frontend-es5/**", "/frontend-es6/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Vaadin has its own cross-site request forgery protection, so disable Spring's
        http.csrf().disable()
            .requestCache()
                .requestCache(new CustomRequestCache())
                .and()
            .authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers(REGISTRATION_URL, "/VAADIN/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and()
            // send users to the logout URL when they log out
            .logout()
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }
}
