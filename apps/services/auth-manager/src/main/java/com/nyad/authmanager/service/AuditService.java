package com.nyad.authmanager.service;

import com.nyad.authmanager.dto.audit.AuditAction;

public interface AuditService {
  void logAudit(String userId, AuditAction action, Class<?> entityClass, String entityId);

  void logAudit(String userId, AuditAction action, String entityName, String entityId);
}
