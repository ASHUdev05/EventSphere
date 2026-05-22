package com.nyad.authmanager.client;

import com.nyad.authmanager.dto.audit.AuditLogRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "log-manager",contextId = "auditClient",path = "/audits")
public interface AuditClient {

  @PostMapping
  ResponseEntity<Void> createAudit(@RequestBody AuditLogRequestDTO dto);
}
