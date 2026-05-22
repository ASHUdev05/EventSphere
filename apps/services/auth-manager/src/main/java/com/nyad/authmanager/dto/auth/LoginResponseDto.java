package com.nyad.authmanager.dto.auth;

public record LoginResponseDto(
  String accessToken,
  String refreshToken,
  String type
)
{}
