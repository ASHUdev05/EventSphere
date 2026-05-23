package com.nyad.authmanager.service;

import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {
  List<UserResponseDto> getAllUsers(String userId);

  List<UserResponseDto> getUsers(List<String> userIds);

  UserResponseDto getUser(String userId);

  UserResponseDto updateUserDetails(String userId, UserRequestDto userRequestDto);

  UserResponseDto changeUserStatus(String userId, String status);

  UserResponseDto changeUserRole(String userId, String role);
}
