package com.nyad.authmanager.dto.auth;

public record LoginRequestDto(
  String password,
  String email
) {
}
