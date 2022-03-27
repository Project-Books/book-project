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
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {
  private final SecretKey secretKey;
  private final JwtConfig jwtConfig;

  public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
    this.secretKey = secretKey;
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
    String tokenPrefix = jwtConfig.getTokenPrefix();

    if (Strings.isNullOrEmpty(authorizationHeader)
        || !authorizationHeader.startsWith(tokenPrefix)) {
      if (request.getMethod().equals(RequestMethod.POST.toString())
          && request.getRequestURI().contains("refreshToken")) {
        Map<String, String> map = readRefreshTokenMapFromRequest(request);
        Claims body = null;
        try {
          body = getJwsClaims(map.get("refreshToken"));
        } catch (Exception e) {
          filterChain.doFilter(request, response);
          return;
        }
        String username = body.getSubject();
        var authorities = (List<Map<String, String>>) body.get("authorities");
        Authentication authentication = getAuthentication(username, authorities);
        this.updateResponseWithJwtAndRefreshToken(response, authentication, secretKey);
        return;
      }
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.replace(tokenPrefix, "");
    try {
      Claims body = getJwsClaims(token);
      String username = body.getSubject();

      var authorities = (List<Map<String, String>>) body.get("authorities");

      Authentication authentication = getAuthentication(username, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (ExpiredJwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
    }

    filterChain.doFilter(request, response);
  }

  private void updateResponseWithJwtAndRefreshToken(
      HttpServletResponse response, Authentication authentication, SecretKey secretKey) {
    String token = jwtConfig.getToken(authentication, secretKey, false);
    String refreshToken = jwtConfig.getToken(authentication, secretKey, true);
    Cookie refreshTokenCookie = jwtConfig.getRefreshTokenCookie(refreshToken);
    response.addCookie(refreshTokenCookie);
    response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
  }

  private static Authentication getAuthentication(
      String username, List<Map<String, String>> authorities) {
    Set<SimpleGrantedAuthority> simpleGrantedAuthorities =
        authorities.stream()
            .map(m -> new SimpleGrantedAuthority(m.get("authority")))
            .collect(Collectors.toSet());

    return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
  }

  private Claims getJwsClaims(String token) {
    Jws<Claims> claimsJws =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    return claimsJws.getBody();
  }

  private static Map<String, String> readRefreshTokenMapFromRequest(HttpServletRequest request)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(request.getInputStream(), Map.class);
  }
}
