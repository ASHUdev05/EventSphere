package com.nyad.authmanager.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
  @Value("${jwt.secret:nvjfenvjnjv53352434rnnc19dnwqdneciu439jn}")
  private String SECRET_KEY;

  @Value("${jwt.access.expiration-in-m:15}")
  private  long ACCESS_EXPIRATION_TIME_IN_M; // Default to 15 minutes

  @Value("${jwt.refresh.expiration-in-d:7}")
  private long REFRESH_EXPIRATION_TIME_IN_D; // Default to 7 days

  /**
   * Derives the HMAC-SHA256 signing key from the configured secret string.
   *
   * @return the {@link SecretKey} used to sign and verify all JWTs
   */
  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  /**
   * Builds a signed JWT for the given user, role, and token type.
   */
  private String buildToken(String userId, String role, TokenType type) {
    var typeString = switch (type){
      case ACCESS -> "ACCESS";
      case REFRESH -> "REFRESH";
    };
    var expirationMillis = switch (type){
      case ACCESS -> ACCESS_EXPIRATION_TIME_IN_M * 60 * 1000;
      case REFRESH -> REFRESH_EXPIRATION_TIME_IN_D * 24 * 60 * 60 * 1000;
    };
    return Jwts.builder()
      .subject(userId)
      .claim("userId", userId)
      .claim("role", role)
      .claim("type", typeString)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expirationMillis))
      // Updated signWith method for JJWT 0.12+
      .signWith(getSigningKey())
      .compact();
  }

  /**
   * Extracts the user ID (JWT subject) from a token.
   */
  public String extractUserId(String token) {
    // Updated parsing for JJWT 0.12+
    return Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  /**
   * Extracts the role from a token's {@code role} claim.
   */
  public String extractRole(String token) {
    return Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .get("role", String.class);
  }

  /**
   * Validates a token's signature, expiration, and type.
   */
  public boolean validateToken(String token, TokenType expectedType) {
    try {
      var claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token);

      var tokenType = claims.getPayload().get("type", String.class);
      return expectedType.name().equals(tokenType);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Parses a token and constructs a {@link UserPrincipal} for use in the Spring Security context.
   */
  UserPrincipal extractUserPrincipal(String token, TokenType expectedType) {
    var claims = Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token);

    var tokenType = claims.getPayload().get("type", String.class);
    if (!expectedType.name().equals(tokenType)) {
      throw new JwtException("Invalid token type. Expected: " + expectedType.name() + ", Found: " + tokenType);
    }

    String role = claims.getPayload().get("role", String.class);
    String userId = claims.getPayload().get("userId", String.class);
    String roleAuthority = "ROLE_" + role.toUpperCase();
    return new UserPrincipal(userId, role, List.of(new SimpleGrantedAuthority(roleAuthority)));
  }

  /**
   * Generates a signed access token for the given user.
   */
  public String generateAccessToken(String userId, String role) {
    return buildToken(userId, role, TokenType.ACCESS);
  }

  /**
   * Generates a signed refresh token for the given user.
   */
  public String generateRefreshToken(String userId, String role) {
    return buildToken(userId, role, TokenType.REFRESH);
  }
}
