package com.nyad.authmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    String path = request.getServletPath();
    if (path.startsWith("/auth/login") || path.startsWith("/auth/register") ||
      path.contains("swagger") || path.contains("api-docs")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        UserPrincipal principal;

        if (path.equals("/auth/refresh")) {
          principal = jwtUtil.extractUserPrincipal(token, TokenType.REFRESH);
        } else {
          principal = jwtUtil.extractUserPrincipal(token, TokenType.ACCESS);
        }

        var authToken = new UsernamePasswordAuthenticationToken(principal, null, principal.authorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    } catch (Exception e) {
      log.error("JWT processing failed: {}", e.getMessage());
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}
