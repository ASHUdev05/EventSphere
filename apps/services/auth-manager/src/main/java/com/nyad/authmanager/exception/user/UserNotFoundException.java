package com.nyad.authmanager.exception.user;

public class UserNotFoundException extends RuntimeException {

  /**
   * Constructs the exception with a caller-supplied detail message.
   *
   * @param message a description of which user was not found (e.g. the user ID or email)
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
