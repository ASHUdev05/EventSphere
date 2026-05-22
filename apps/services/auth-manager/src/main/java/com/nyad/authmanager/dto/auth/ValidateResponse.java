package com.nyad.authmanager.dto.auth;

public record ValidateResponse(
  String userId,
  String userRole
) {
}
