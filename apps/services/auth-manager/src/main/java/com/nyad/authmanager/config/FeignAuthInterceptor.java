package com.nyad.authmanager.config;

import com.nyad.authmanager.security.JwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

  private final JwtUtil jwtUtil;

  /** The pseudo-UUID that represents this service in the JWT {@code userId} claim. */
  @Value("${system.user-id:00000000-0000-0000-0000-000000000000}")
  private String systemUserId;

  /** The role embedded in the system token (must be recognizable by downstream services). */
  @Value("${system.role:ADMIN}")
  private String systemRole;

  public FeignAuthInterceptor(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Intercepts every outgoing Feign request and appends a freshly generated
   * system {@code Authorization: Bearer <token>} header.
   *
   * @param template The {@link RequestTemplate} for the outgoing Feign request.
   */
  @Override
  public void apply(RequestTemplate template) {
    String systemToken = jwtUtil.generateAccessToken(systemUserId, systemRole);
    template.header("Authorization", "Bearer " + systemToken);
  }
}
