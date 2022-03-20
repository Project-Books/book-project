/*
   The book project lets a user keep track of different books they would like to read, are currently
   reading, have read or did not finish.
   Copyright (C) 2021  Karan Kumar
   This program is free software: you can redistribute it and/or modify it under the terms of the
   GNU General Public License as published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.
   This program is distributed in the hope that it will be useful, but WITHOUT ANY
   WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
   PURPOSE.  See the GNU General Public License for more details.
   You should have received a copy of the GNU General Public License along with this program.
   If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.karankumar.bookproject.security.jwt.JwtConfig;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@Tag("Integration")
class JwtTokenVerifierIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private JwtConfig jwtConfig;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private SecretKey key;

  @Test
  void shouldReturnUnauthorizedIfIncorrectRefreshTokenPassed() throws Exception {
    // given
    String url = "http://localhost:8080/refreshToken";

    Map<String, String> map = new HashMap<>();
    map.put(
        "refreshToken",
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQHVzZXIudXNlciIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJVU0VSIn1dLCJpYXQiOjE2MzQ5MTc5NzMsImV4cCI6MTYzNTcwNTAwMH0.yZ8NSDTCXvQZN7Bdv_-BmQBHyiJIPJhwdqNg9G5lXQUap4Nk2N");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = writer.writeValueAsString(map);

    // when & then
    this.mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andDo(document("refreshToken"));
  }

  @Test
  void shouldReturnUnauthorizedIfTokenNotPassed() throws Exception {
    // given
    String url = "http://localhost:8080/api/books";

    // when & then
    this.mockMvc
        .perform(get(url))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andDo(document("books"));
  }

  @Test
  void shouldReturnUnauthorizedIfTokenInvalidTokenPassed() {
    try {
      // given
      String url = "http://localhost:8080/api/books";

      // when & then
      this.mockMvc
          .perform(get(url).header("Authorization", "Bearer abc"))
          .andDo(print())
          .andExpect(status().is4xxClientError())
          .andDo(document("books"));
    } catch (Exception e) {
    }
  }

  @Test
  void shouldAllowRequestIfTokenIsValid() throws Exception {
    // given
    Authentication authentication =
        new UsernamePasswordAuthenticationToken("user@user.user", "password");
    Authentication authentication1 = authenticationManager.authenticate(authentication);
    String token =
        Jwts.builder()
            .setSubject(authentication1.getName())
            .claim("authorities", authentication1.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(
                java.sql.Date.valueOf(
                    LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
            .signWith(key)
            .compact();
    String url = "http://localhost:8080/api/books";

    // when & then
    this.mockMvc
        .perform(get(url).header("Authorization", "Bearer " + token))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andDo(document("login"));
  }

  @Test
  void shouldGetUnauthenticatedIfTokenIsExpired() throws Exception {
    // given
    Authentication authentication =
        new UsernamePasswordAuthenticationToken("user@user.user", "password");
    Authentication authentication1 = authenticationManager.authenticate(authentication);
    String token =
        Jwts.builder()
            .setSubject(authentication1.getName())
            .claim("authorities", authentication1.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + 1))
            .signWith(key)
            .compact();
    String url = "http://localhost:8080/api/books";

    // when & then
    this.mockMvc
        .perform(get(url).header("Authorization", "Bearer " + token))
        .andDo(print())
        .andExpect(status().is(401))
        .andDo(document("login"));
  }

  @Test
  void shouldReturnJwtAndRefreshTokenWhenProvidedCorrectRefreshToken() throws Exception {
    // given
    Authentication authentication =
        new UsernamePasswordAuthenticationToken("user@user.user", "password");
    Authentication authentication1 = authenticationManager.authenticate(authentication);
    String token =
        Jwts.builder()
            .setSubject(authentication1.getName())
            .claim("authorities", authentication1.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(
                java.sql.Date.valueOf(
                    LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
            .signWith(key)
            .compact();
    Map<String, String> map = new HashMap<>();
    map.put("refreshToken", token);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = writer.writeValueAsString(map);
    String url = "http://localhost:8080/refreshToken";

    // when & then
    this.mockMvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andDo(document("login"));
  }
}
