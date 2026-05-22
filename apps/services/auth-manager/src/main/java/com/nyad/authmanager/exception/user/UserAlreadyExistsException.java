package com.nyad.authmanager.exception.user;

public class UserAlreadyExistsException extends RuntimeException {

  /**
   * Constructs the exception with a detail message identifying the duplicate email.
   *
   * @param email the email address that is already registered in the system
   */
  public UserAlreadyExistsException(String email) {
    super("User with email " + email + " already exists.");
  }
}
