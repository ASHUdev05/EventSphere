package com.nyad.authmanager.controller;

import com.nyad.authmanager.dto.auth.LoginRequestDto;
import com.nyad.authmanager.dto.auth.LoginResponseDto;
import com.nyad.authmanager.dto.auth.RegisterResponseDto;
import com.nyad.authmanager.dto.auth.ValidateResponse;
import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.security.UserPrincipal;
import com.nyad.authmanager.service.AuthService;
import com.nyad.authmanager.service.UserService;
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

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> register(@RequestBody UserRequestDto dto) {
    return ResponseEntity.ok(authService.register(dto));
  }

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

  @GetMapping("/validate")
  public ResponseEntity<ValidateResponse> validate(@RequestHeader("Authorization") String authHeader) {
    return ResponseEntity.ok(authService.validateToken(authHeader));
  }

}
