package com.vux.security.service.impl;

import com.vux.security.entity.Token;
import com.vux.security.entity.User;
import com.vux.security.repository.TokenRepository;
import com.vux.security.repository.UserRepository;
import com.vux.security.payload.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vux.security.service.EmailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailService emailService;


  public AuthenticationResponse register(RegisterRequest request) throws MessagingException, TemplateException, IOException {
	Random rand = new Random();
    LocalTime checkinTime = LocalTime.now().withHour(8).withMinute(30).withSecond(0).withNano(0);
    
    LocalTime checkoutTime = LocalTime.now().withHour(17).withMinute(30).withSecond(0).withNano(0);
    var user = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.EMPLOYEE)
        .authProvider(AuthProvider.LOCAL)
        .codeCheck(String.valueOf(rand.nextInt((9999 - 1000) + 1) + 1000)) // random code check in range 1000-9999
        .timeCheckin(checkinTime)
        .timeCheckout(checkoutTime)
        .build();



    var savedUser = repository.save(user);

    UserDetailsImpl userDetails = entityMapUserDetails(user);

    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);

    emailService.sendSimpleMessage(user.getEmail(), "Register Success", "Code checkin/checkout is: " + user.getCodeCheck(), user);
    
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  private UserDetailsImpl entityMapUserDetails(User user) {
    return UserDetailsImpl.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) throws ChangeSetPersister.NotFoundException {

    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(ChangeSetPersister.NotFoundException::new);

    UserDetailsImpl userDetails = entityMapUserDetails(user);
    authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                     request.getEmail(),
                     request.getPassword()
             )
     );

    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);

    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);

    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      UserDetailsImpl userDetails = entityMapUserDetails(user);
      if (jwtService.isTokenValid(refreshToken, userDetails)) {
        var accessToken = jwtService.generateToken(userDetails);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
