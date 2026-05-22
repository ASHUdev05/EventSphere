package com.nyad.authmanager.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserPrincipal(
  String userId,
  String role,
  Collection<? extends GrantedAuthority> authorities
)
{}
