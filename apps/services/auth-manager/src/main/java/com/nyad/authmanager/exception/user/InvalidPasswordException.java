package com.nyad.authmanager.exception.user;

public class InvalidPasswordException extends RuntimeException {

  /**
   * Constructs the exception with a caller-supplied detail message.
   *
   * @param message a description of the password validation failure
   */
  public InvalidPasswordException(String message) {
    super(message);
  }
}
