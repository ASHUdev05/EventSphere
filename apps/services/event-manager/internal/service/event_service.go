package service

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/mapper"
	"eventsphere/event-manager/internal/model/data"
	"eventsphere/event-manager/internal/repository"
	"log"
)

type EventService struct {
	Repo   *repository.EventRepository
	Audit  *AuditService
	Notify *NotificationService
}

func (s *EventService) FindById(id string, userID string) (dto.EventResponseDto, error) {
	eventEntity, err := s.Repo.FindById(id)
	if err != nil {
		log.Printf("Failed to find event by ID: %v", err)
		return dto.EventResponseDto{}, err
	}

	s.Audit.LogAudit(userID, data.ActionRead, "Event", id)

	resp := mapper.ToEventResponse(*eventEntity)
	return resp, nil
}

func (s *EventService) Create(userID string, req dto.EventRequestDto) dto.EventResponseDto {
	event := mapper.ToEventEntity(req)

	if err := s.Repo.DB.Create(&event).Error; err != nil {
		log.Printf("Failed to save event: %v", err)
		return dto.EventResponseDto{} // Or handle error appropriately
	}

	log.Printf("Successfully saved event with ID: %s", event.EventID)

	s.Audit.LogAudit(userID, data.ActionCreate, "Event", event.EventID)
	s.Notify.SendNotification(req.OrganizerID, "New Event Created: "+req.Name, "EVENT")

	return mapper.ToEventResponse(event)
}
