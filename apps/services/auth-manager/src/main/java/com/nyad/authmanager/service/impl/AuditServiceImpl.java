package com.nyad.authmanager.service.impl;

import com.nyad.authmanager.client.AuditClient;
import com.nyad.authmanager.dto.audit.AuditAction;
import com.nyad.authmanager.dto.audit.AuditLogRequestDTO;
import com.nyad.authmanager.service.AuditService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {
  private final AuditClient auditClient;

  @Override
  public void logAudit(String userId, AuditAction action, Class<?> entityClass, String entityId) {
    logAudit(userId, action, entityClass.getSimpleName(), entityId);
  }

  @Override
  public void logAudit(String userId, AuditAction action, String entityName, String entityId) {
    var dto = new AuditLogRequestDTO(action, entityId, entityName);
    log.debug("Sending audit request: userId={}, payload={}", userId, dto);
    try {
      auditClient.createAudit(dto);
      log.debug("Audit logged successfully: userId={}, action={}, entity={}, entityId={}",
        userId, action, entityName, entityId);
    } catch (FeignException e) {
      log.warn("Audit call rejected by log-manager: userId={}, action={}, entity={}, entityId={}" +
          " | HTTP status={}, response body={}",
        userId, action, entityName, entityId,
        e.status(), e.contentUTF8());
    } catch (Exception e) {
      log.warn("Audit call failed (connection/unexpected error): userId={}, action={}, entity={}, entityId={}" +
          " | error={}",
        userId, action, entityName, entityId, e.getMessage());
    }
  }
}
