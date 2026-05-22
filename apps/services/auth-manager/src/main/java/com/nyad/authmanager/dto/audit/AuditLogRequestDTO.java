package com.nyad.authmanager.dto.audit;

public record AuditLogRequestDTO(
  AuditAction action,
  String entityId,
  String entityName
) {
}
