package com.nyad.authmanager.exception.user;

public class UserSuspendedException extends RuntimeException {

  /**
   * Constructs the exception with a detail message identifying the suspended user.
   *
   * @param userId the UUID of the user whose account is suspended
   */
  public UserSuspendedException(String userId){
    super(String.format("User with id %s is suspended", userId));
  }
}
