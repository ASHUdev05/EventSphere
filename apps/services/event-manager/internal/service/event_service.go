package service

import (
	"errors"
	"fmt"
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

func (s *EventService) FindById(id string, userID string) (any, any) {
	eventAny, err := s.Repo.FindById(id)
	if err != nil {
		log.Printf("Failed to find event by ID: %v", err)
		return nil, err
	}

	var ev data.Event
	switch v := eventAny.(type) {
	case data.Event:
		ev = v
	case *data.Event:
		ev = *v
	default:
		log.Printf("Unexpected event type returned: %T", v)
		return nil, fmt.Errorf("unexpected event type: %T", v)
	}

	s.Audit.LogAudit(userID, data.ActionRead, "Event", id)

	// convert to response
	resp := mapper.ToEventResponse(ev)
	if resp == nil {
		return nil, errors.New("failed to map event to response")
	}

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
