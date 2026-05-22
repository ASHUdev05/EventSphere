package com.nyad.authmanager.dto.auth;

public record RegisterResponseDto(
  String userId,
  String userName,
  String userEmail,
  String role,
  String phoneNo,
  String userStatus,
  String message
) {
}
