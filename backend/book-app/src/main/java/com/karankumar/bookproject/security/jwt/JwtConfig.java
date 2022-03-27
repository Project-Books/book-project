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

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.Date;
import io.jsonwebtoken.Jwts;

@ConfigurationProperties("application.jwt")
@NoArgsConstructor
@Getter
@Setter
public class JwtConfig {
  private String secretKey;
  private String tokenPrefix;
  private Integer tokenExpirationAfterDays;
  private Integer refreshExpirationAfterDays;

  public String getAuthorizationHeader() {
    return HttpHeaders.AUTHORIZATION;
  }

  public String getToken(Authentication authResult, SecretKey key, boolean isRefreshToken) {
    if (isRefreshToken) {
      return createToken(authResult, key, this.refreshExpirationAfterDays);
    }
    return createToken(authResult, key, this.tokenExpirationAfterDays);
  }

  private static String createToken(
      Authentication authResult, SecretKey key, Integer tokenExpirationAfterDays) {
    return Jwts.builder()
        .setSubject(authResult.getName())
        .claim("authorities", authResult.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(tokenExpirationAfterDays)))
        .signWith(key)
        .compact();
  }

  public Cookie getRefreshTokenCookie(String refreshToken) {
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(this.getRefreshExpirationAfterDays());
    return cookie;
  }
}
