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

package com.karankumar.bookproject.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.karankumar.bookproject.security.jwt.exceptionhandler.ApiExceptionHandler;
import com.karankumar.bookproject.security.jwt.exceptionhandler.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.Cookie;

public class JwtUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JwtConfig jwtConfig;
  private final SecretKey secretKey;

  public JwtUsernamePasswordAuthFilter(
      AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
    this.authenticationManager = authenticationManager;
    this.jwtConfig = jwtConfig;
    this.secretKey = secretKey;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      UsernamePasswordAuthRequest authenticationRequest =
          new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordAuthRequest.class);

      Authentication authentication =
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.getUsername(), authenticationRequest.getPassword());
      return authenticationManager.authenticate(authentication);
    } catch (LockedException e) {
      handleLockedException(request, response, e);
      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    String token = jwtConfig.getToken(authResult, secretKey, false);
    String refreshToken = jwtConfig.getToken(authResult, secretKey, true);
    Cookie cookie = jwtConfig.getRefreshTokenCookie(refreshToken);
    response.addCookie(cookie);

    response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
  }

  private void handleLockedException(
      HttpServletRequest request, HttpServletResponse response, LockedException e) {
    try {
      HttpStatus status = HttpStatus.FORBIDDEN;

      ErrorResponse errorResponse =
          ApiExceptionHandler.createErrorResponse(status)
              .error("Authentication Error")
              .message(e.getMessage())
              .build();

      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setCharacterEncoding("utf-8");
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      String errorJson = objectMapper.writeValueAsString(errorResponse);

      PrintWriter printWriter = response.getWriter();
      printWriter.println(errorJson);
      printWriter.flush();
      printWriter.close();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
