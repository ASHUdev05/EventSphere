package com.nyad.authmanager.service.impl;

import com.nyad.authmanager.dto.audit.AuditAction;
import com.nyad.authmanager.dto.user.UserRequestDto;
import com.nyad.authmanager.dto.user.UserResponseDto;
import com.nyad.authmanager.exception.user.EmailAlreadyExistsException;
import com.nyad.authmanager.exception.user.UserNotFoundException;
import com.nyad.authmanager.mapper.UserResponseDtoMapper;
import com.nyad.authmanager.model.User;
import com.nyad.authmanager.model.data.UserRole;
import com.nyad.authmanager.model.data.UserStatus;
import com.nyad.authmanager.repository.UserRepository;
import com.nyad.authmanager.service.AuditService;
import com.nyad.authmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final AuditService auditService;

  @Override
  public List<UserResponseDto> getAllUsers(String actorId) {
    List<User> userList = userRepo.findAll();
    return userList
      .stream()
      .peek(event -> auditService.logAudit(actorId, AuditAction.READ,User.class,event.getUserID()))
      .map(UserResponseDtoMapper::toDTO).toList();
  }

  @Override
  public List<UserResponseDto> getUsers(List<String> userIds) {
    return userRepo.findAllById(userIds).stream()
      .map(UserResponseDtoMapper::toDTO)
      .toList();
  }

  @Override
  public UserResponseDto getUser(String userId) {
    User user =userRepo.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
    auditService.logAudit(userId,AuditAction.READ,User.class,userId);
    return UserResponseDtoMapper.toDTO(user);
  }

  @Override
  public UserResponseDto updateUserDetails(String userId, UserRequestDto userRequestDto) {
    if(!userRepo.existsById(userId)){
      throw new IllegalArgumentException("User with user id "+userId+" does not exist");
    }
    User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundException(userId));


    if (userRequestDto.email() != null && !userRequestDto.email().equalsIgnoreCase(user.getEmail())) {
      if (userRepo.existsByEmail(userRequestDto.email())) {
        throw new EmailAlreadyExistsException(userRequestDto.email());
      }
      user.setEmail(userRequestDto.email());
    }

    if (userRequestDto.name() != null) user.setName(userRequestDto.name());
    if (userRequestDto.phone() != null) user.setPhone(userRequestDto.phone());

    if (userRequestDto.password() != null && !userRequestDto.password().isBlank()) {
      String hashed = passwordEncoder.encode(userRequestDto.password());
      user.setPassword(hashed);
    }

    User saved = userRepo.save(user);
    auditService.logAudit(user.getUserID(),AuditAction.UPDATE,User.class,saved.getUserID());
    return UserResponseDtoMapper.toDTO(saved);
  }

  @Override
  public UserResponseDto changeUserStatus(String userId, String status) {
    User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    String enumStatus = String.valueOf(UserStatus.valueOf(status));
    user.setStatus(UserStatus.valueOf(enumStatus));
    auditService.logAudit(user.getUserID(),AuditAction.UPDATE,User.class,userId);
    return UserResponseDtoMapper.toDTO(userRepo.save(user));
  }

  @Override
  public UserResponseDto changeUserRole(String userId, String role) {
    User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    log.info("changing user role to {} for {}",  role, userId);
    UserRole newRole = UserRole.valueOf(role);
    user.setRole(newRole);
    User updatedUser = userRepo.save(user);
    log.info("changing user role to {} for {}",  newRole.name(), userId);
    auditService.logAudit(userId,AuditAction.UPDATE,User.class,updatedUser.getUserID());
    try{
      String message = "Your role has been changed to " + newRole;
//            notificationService.sendNotification(userId, message, "INFO");
      log.info("Role change notification queued for user: {}", userId);
    }
    catch (Exception e){
      log.error("Failed to send notification for role change to {} : {}", userId, e.getMessage());
    }
    return UserResponseDtoMapper.toDTO(updatedUser);
  }
}
