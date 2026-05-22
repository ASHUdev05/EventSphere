package com.nyad.authmanager.exception.user;

public class UserNotActiveException extends RuntimeException {

  /**
   * Constructs the exception with a detail message identifying the inactive user.
   *
   * @param userId the UUID of the user whose account is not active
   */
  public UserNotActiveException(String userId){
    super(String.format("User with id %s is not active", userId));
  }
}
