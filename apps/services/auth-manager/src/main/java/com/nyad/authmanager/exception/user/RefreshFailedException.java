package com.nyad.authmanager.exception.user;

public class RefreshFailedException extends RuntimeException {

  /**
   * Constructs the exception with a detail message prompting the user to log in again.
   *
   * @param userId the UUID of the user for whom the refresh attempt failed
   */
  public RefreshFailedException(String userId){
    super(String.format("Failed to authenticate user: %s, please login again", userId));
  }
}
