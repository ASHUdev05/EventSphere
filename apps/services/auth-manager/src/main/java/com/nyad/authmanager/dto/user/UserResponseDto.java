package com.nyad.authmanager.dto.user;

import com.nyad.authmanager.model.data.UserRole;
import com.nyad.authmanager.model.data.UserStatus;

public record UserResponseDto(
  String userId,
  String name,
  UserRole role,
  String email,
  String phone,
  UserStatus status
)
{}
