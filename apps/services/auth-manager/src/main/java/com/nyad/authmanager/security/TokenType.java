package com.nyad.authmanager.security;

public enum TokenType {
  /** Short-lived JWT used to authorize API requests. */
  ACCESS,

  /** Long-lived JWT used exclusively to refresh an expired access token. */
  REFRESH
}
