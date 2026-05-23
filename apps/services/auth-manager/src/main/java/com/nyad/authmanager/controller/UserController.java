package com.nyad.authmanager.controller;

import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.dto.user.UserResponseDto;
import com.nyad.authmanager.security.UserPrincipal;
import com.nyad.authmanager.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {
  private final UserService userService;

  @GetMapping("/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponseDto>> getAllUsers(
    @AuthenticationPrincipal UserPrincipal principal
  ){
    return ResponseEntity.ok(userService.getAllUsers(principal.userId()));
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId){
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @PutMapping("/users/{userId}")
  @PreAuthorize("hasRole('ADMIN') or principal.userId().equals(#userId)")
  public ResponseEntity<UserResponseDto> updateUserDetails(@PathVariable String userId, @RequestBody UserRequestDto userRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
    return ResponseEntity.ok(userService.updateUserDetails(userId,userRequestDto));
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserResponseDto> getMyDetails(@AuthenticationPrincipal UserPrincipal userPrincipal){
    String authenticatedUserId = userPrincipal.userId();
    return ResponseEntity.ok(userService.getUser(authenticatedUserId));
  }

  @PatchMapping("/users/{userId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> changeUserStatus(@PathVariable String userId, @RequestParam String status) {
    UserResponseDto updatedUserStatus = userService.changeUserStatus(userId, status);
    return ResponseEntity.ok(updatedUserStatus);
  }

  @PatchMapping("/users/{userId}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> changeUserRole(@PathVariable String userId, @RequestParam String role) {
    UserResponseDto updatedUserRole = userService.changeUserRole(userId, role);
    return ResponseEntity.ok(updatedUserRole);
  }

  @PostMapping("/users/userdetails")
  public ResponseEntity<List<UserResponseDto>> getUserDetails(@RequestBody List<String> userIds) {
    return ResponseEntity.ok(userService.getUsers(userIds));
  }

}
