package service

import (
	"eventsphere/event-manager/internal/client"
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/model/data"
)

type AuditService struct {
	Client *client.AuditClient
}

func (s *AuditService) LogAudit(userID string, action data.AuditAction, entityName string, entityID string) {
	req := dto.AuditLogRequestDTO{
		Action:     action,
		EntityID:   entityID,
		EntityName: entityName,
	}
	s.Client.CreateAudit(nil, req) // ctx is nil if called from background/exception
}