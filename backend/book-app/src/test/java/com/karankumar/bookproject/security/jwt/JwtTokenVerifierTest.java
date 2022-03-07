package com.karankumar.bookproject.security.jwt;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class JwtTokenVerifierTest {

  private static final String EMAIL = "test@gmail.com";

  private final JwtConfig jwtConfig = Mockito.mock(JwtConfig.class);
  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final SecretKey secretKey =
      Keys.hmacShaKeyFor(
          "adkasdjaadkasdjaadkasdjaadkasdjaadkasdjaadkasdjaadkasdjaadkasdjaadkasdja".getBytes(
              StandardCharsets.UTF_8)
      );
  private final JwtTokenVerifier jwtTokenVerifier = new JwtTokenVerifier(secretKey, jwtConfig,
      userRepository);

  @Test
  void shouldFailWhenUsingTokenForUserDeletedFromTheSystem() throws ServletException, IOException {
    // given
    final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    final FilterChain filterChain = Mockito.mock(FilterChain.class);
    final String token = createToken(
        Date.from(Instant.now().minus(5, ChronoUnit.DAYS)),
        Date.from(Instant.now().plus(5, ChronoUnit.DAYS))
    );
    given(jwtConfig.getAuthorizationHeader())
        .willReturn("Authorization");
    given(request.getHeader(jwtConfig.getAuthorizationHeader()))
        .willReturn("Bearer " + token);
    given(jwtConfig.getTokenPrefix())
        .willReturn("Bearer");
    given(userRepository.findByEmail("test@gmail.com"))
        .willReturn(Optional.empty());

    // when
    jwtTokenVerifier.doFilterInternal(request, response, filterChain);

    // then
    verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void shouldFailWhenUsingTokenForUserAfterPasswordChange() throws ServletException, IOException {
    // given
    final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    final FilterChain filterChain = Mockito.mock(FilterChain.class);
    final String token = createToken(
        Date.from(Instant.now().minus(5, ChronoUnit.DAYS)),
        Date.from(Instant.now().plus(5, ChronoUnit.DAYS))
    );
    given(jwtConfig.getAuthorizationHeader())
        .willReturn("Authorization");
    given(request.getHeader(jwtConfig.getAuthorizationHeader()))
        .willReturn("Bearer " + token);
    given(jwtConfig.getTokenPrefix())
        .willReturn("Bearer");
    given(userRepository.findByEmail(EMAIL))
        .willReturn(
            Optional.of(
                User.builder()
                    .lastPasswordChangeTime(LocalDateTime.now().minusDays(1))
                    .build()
            )
        );

    // when
    jwtTokenVerifier.doFilterInternal(request, response, filterChain);

    // then
    verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  private String createToken(Date issueDate, Date expirationDate) {
    return Jwts.builder()
        .setSubject(EMAIL)
        .setIssuedAt(issueDate)
        .setExpiration(expirationDate)
        .signWith(secretKey)
        .compact();
  }

}