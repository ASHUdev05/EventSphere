package com.nyad.authmanager.dto.user;

public record UserRequestDto(
  String name,
  String email,
  String password,
  String phone
)
{}
