package com.nyad.authmanager.service;

import com.nyad.authmanager.dto.auth.LoginRequestDto;
import com.nyad.authmanager.dto.auth.LoginResponseDto;
import com.nyad.authmanager.dto.auth.RegisterResponseDto;
import com.nyad.authmanager.dto.auth.ValidateResponse;
import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.security.UserPrincipal;

public interface AuthService {
  RegisterResponseDto register(UserRequestDto dto);

  LoginResponseDto login(LoginRequestDto loginDto);

  LoginResponseDto refreshToken(UserPrincipal principal);

  ValidateResponse validateToken(String authHeader);
}
