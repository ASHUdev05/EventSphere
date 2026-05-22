package com.nyad.authmanager.controller;

import com.nyad.authmanager.dto.auth.LoginRequestDto;
import com.nyad.authmanager.dto.auth.LoginResponseDto;
import com.nyad.authmanager.dto.auth.RegisterResponseDto;
import com.nyad.authmanager.dto.auth.ValidateResponse;
import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  /**
   * Registers a new user account.
   *
   * <p>{@code POST /api/v1/auth/register}</p>
   *
   * @param dto the registration payload containing name, email, password, and phone
   * @return HTTP 200 OK with a {@link RegisterResponseDto} containing the new user's profile
   *         and a confirmation message
   */
  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> register(@RequestBody UserRequestDto dto) {
    return ResponseEntity.ok(authService.register(dto));
  }

  /**
   * Authenticates a user with their email and password.
   *
   * <p>{@code POST /api/v1/auth/login}</p>
   *
   * @param dto the login credentials (email and password)
   * @return HTTP 200 OK with a {@link LoginResponseDto} containing access token, refresh token,
   *         and token type
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
    var response = authService.login(dto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponseDto> refreshToken(@AuthenticationPrincipal UserPrincipal principal) {
    var response = authService.refreshToken(principal);
    return ResponseEntity.ok(response);
  }

  /**
   * Validates an access token and returns the user ID and role it carries.
   *
   * <p>{@code GET /api/v1/auth/validate} — called by the API Gateway or downstream
   * microservices to verify an incoming Bearer token before forwarding the request.</p>
   *
   * @param authHeader the full {@code Authorization} header value (e.g. {@code "Bearer <token>"})
   * @return HTTP 200 OK with a {@link ValidateResponse} containing the user ID and role
   */
  @GetMapping("/validate")
  public ResponseEntity<ValidateResponse> validate(@RequestHeader("Authorization") String authHeader) {
    return ResponseEntity.ok(authService.validateToken(authHeader));
  }

}
