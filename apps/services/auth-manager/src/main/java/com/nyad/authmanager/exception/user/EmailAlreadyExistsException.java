package com.nyad.authmanager.exception.user;

public class EmailAlreadyExistsException extends RuntimeException{

  /**
   * Constructs the exception with a detail message identifying the duplicate email.
   *
   * @param email the email address that already exists in the system
   */
  public EmailAlreadyExistsException(String email) {
    super("Email " + email + " is already registered.");
  }
}
