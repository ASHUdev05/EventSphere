package com.nyad.authmanager.exception;

import com.nyad.authmanager.dto.audit.AuditAction;
import com.nyad.authmanager.exception.general.GenericErrorResponse;
import com.nyad.authmanager.exception.user.*;
import com.nyad.authmanager.model.User;
import com.nyad.authmanager.security.UserPrincipal;
import com.nyad.authmanager.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {


  private final AuditService auditService;


  private String resolveUserId() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
      return principal.userId();
    }
    return "anonymous";
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<GenericErrorResponse> emailAlreadyExistsException(EmailAlreadyExistsException e, HttpServletRequest request) {
    auditService.logAudit(resolveUserId(), AuditAction.REGISTRATON_FAILURE,User.class,request.getRequestURI());
    return new ResponseEntity<>(new GenericErrorResponse("Email already exists"), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<GenericErrorResponse> invalidPasswordException(InvalidPasswordException e,HttpServletRequest request){
    auditService.logAudit(resolveUserId(),AuditAction.LOGIN_FAILURE, User.class,request.getRequestURI());
    return new ResponseEntity<>(new GenericErrorResponse("Invalid password"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RefreshFailedException.class)
  public ResponseEntity<GenericErrorResponse> handleRefreshFailedException(RefreshFailedException e, HttpServletRequest request){
    auditService.logAudit(resolveUserId(),AuditAction.PERMISSION_CHANGE,User.class,request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<GenericErrorResponse> userAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request){
    auditService.logAudit(resolveUserId(),AuditAction.REGISTRATON_FAILURE,User.class,request.getRequestURI());
    return new ResponseEntity<>(new GenericErrorResponse("User already exists"), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotActiveException.class)
  public ResponseEntity<GenericErrorResponse> handleUserNotActiveException(UserNotActiveException e,HttpServletRequest request){
    auditService.logAudit(resolveUserId(),AuditAction.ACCESS_DENIED,User.class,request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<GenericErrorResponse> userNotFoundException(UserNotFoundException e, HttpServletRequest request){
    auditService.logAudit(resolveUserId(), AuditAction.LOGIN_FAILURE, User.class,request.getRequestURI());
    return new ResponseEntity<>(new GenericErrorResponse("User not found"), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserSuspendedException.class)
  public ResponseEntity<GenericErrorResponse> handleUserSuspendedException(UserSuspendedException e, HttpServletRequest request){
    auditService.logAudit(resolveUserId(),AuditAction.ACCESS_DENIED,User.class,request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse(e.getMessage()));
  }
}
