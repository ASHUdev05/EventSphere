package com.nyad.authmanager.service.impl;

import com.nyad.authmanager.dto.audit.AuditAction;
import com.nyad.authmanager.dto.auth.LoginRequestDto;
import com.nyad.authmanager.dto.auth.LoginResponseDto;
import com.nyad.authmanager.dto.auth.RegisterResponseDto;
import com.nyad.authmanager.dto.auth.ValidateResponse;
import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.exception.user.*;
import com.nyad.authmanager.model.User;
import com.nyad.authmanager.model.data.UserStatus;
import com.nyad.authmanager.repository.UserRepository;
import com.nyad.authmanager.security.JwtUtil;
import com.nyad.authmanager.security.TokenType;
import com.nyad.authmanager.security.UserPrincipal;
import com.nyad.authmanager.service.AuditService;
import com.nyad.authmanager.service.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final AuditService auditService;

  @Override
  public RegisterResponseDto register(UserRequestDto dto) {
    var existingUser = userRepository.findByEmail(dto.email());

    if (existingUser.isPresent()) {
//            auditService.logAudit(existingUser.getUserId(), AuditAction.REGISTRATION_FAILURE, User.class, user.getUserId());
//            auditService.logAudit(existingUser.get().getUserId(), AuditAction.REGISTRATON_FAILURE,User.class,existingUser.get().getUserId());
      throw new UserAlreadyExistsException(dto.email());
    }

    User user = new User();
    user.setName(dto.name());
    user.setEmail(dto.email());
    user.setPhone(dto.phone());
    user.setPassword(passwordEncoder.encode(dto.password())); // Hashing
    userRepository.save(user);
    log.info("User {} registered with id {}", user.getName(),user.getUserID());
    auditService.logAudit(user.getUserID(), AuditAction.REGISTRATION_SUCCESS,User.class,user.getUserID());
    String successRegistration = "User registered successfully with email: " + user.getEmail();
    return new RegisterResponseDto(user.getUserID(), user.getName(), user.getEmail(), user.getRole().name(), user.getPhone(), user.getStatus().name(), successRegistration);
  }

  @Override
  public LoginResponseDto login(LoginRequestDto loginDto) {
    User user = userRepository.findByEmail(loginDto.email())
      .orElseThrow(() ->{
        log.warn("Login failed for email: {} - user not found", loginDto.email());
//                    auditService.logAudit(user.getUserId(),AuditAction.LOGIN_FAILURE,User.class,user.getUserId());
        return new UserNotFoundException(loginDto.email());
      });

    if(loginDto.email() == user.getEmail()){
      log.error("Login failed for email: {} - user already exists", loginDto.email());
      throw new EmailAlreadyExistsException(loginDto.email());
    }

    if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
      log.warn("Login failed: invalid password");
//            auditService.logAudit(user.getUserId(),AuditAction.LOGIN_FAILURE,User.class,user.getUserId());
      throw new InvalidPasswordException("Invalid password provided");
    }

    String roleName = user.getRole().name();

    String accessToken = jwtUtil.generateAccessToken(user.getUserID(), roleName);
    String refreshToken = jwtUtil.generateRefreshToken(user.getUserID(), roleName);

    auditService.logAudit(user.getUserID(), AuditAction.LOGIN_SUCCESS, User.class, user.getUserID());
    return new LoginResponseDto(accessToken, refreshToken, "Bearer");
  }

  @Override
  public LoginResponseDto refreshToken(UserPrincipal principal) {
    String userId = principal.userId();
//        String email = principal.email();
    String roleName = principal.role();
    var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    if(!user.getRole().name().equals(roleName)){
//            auditService.logAudit(userId,AuditAction.PERMISSION_CHANGE,User.class,user.getUserId());
      throw new RefreshFailedException(userId);
    }

    roleName = user.getRole().name();

    if(user.getStatus().equals(UserStatus.INACTIVE)){
//            auditService.logAudit(userId,AuditAction.STATUS_CHANGE,User.class,user.getUserId());
      throw new UserNotActiveException(userId);
    }

    if(user.getStatus().equals(UserStatus.SUSPENDED)){
      throw new UserSuspendedException(userId);
    }

    String newAccessToken = jwtUtil.generateAccessToken(userId, roleName);
    String newRefreshToken = jwtUtil.generateRefreshToken(userId, roleName);

    return new LoginResponseDto(newAccessToken, newRefreshToken, "Bearer");
  }

  @Override
  public ValidateResponse validateToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
    }

    String token = authHeader.substring(7);

    try {
      if (!jwtUtil.validateToken(token, TokenType.ACCESS)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
      }

      String userId = jwtUtil.extractUserId(token);
      String role = jwtUtil.extractRole(token);

      return new ValidateResponse(userId, role);

    } catch (JwtException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
  }
}
