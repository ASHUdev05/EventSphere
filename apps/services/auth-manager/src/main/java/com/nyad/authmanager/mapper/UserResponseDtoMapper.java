package com.nyad.authmanager.mapper;

import com.nyad.authmanager.dto.user.UserResponseDto;
import com.nyad.authmanager.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseDtoMapper {

  /**
   * Converts a {@link User} entity to a {@link UserResponseDto}, omitting sensitive fields
   * such as password and timestamps.
   *
   * @param user the {@link User} entity to convert; must not be {@code null}
   * @return a {@link UserResponseDto} populated with the user's public profile data
   */
  public static UserResponseDto toDTO(User user){
    return new UserResponseDto(
      user.getUserID(),
      user.getName(),
      user.getRole(),
      user.getEmail(),
      user.getPhone(),
      user.getStatus()
    );
  }

}
