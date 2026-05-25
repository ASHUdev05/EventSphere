package service

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/exception"
	"eventsphere/event-manager/internal/mapper"
	"eventsphere/event-manager/internal/model"
	"eventsphere/event-manager/internal/model/data"
	"eventsphere/event-manager/internal/repository"
)

type RegistrationService struct {
	Repo   *repository.RegistrationRepository
	Ticket *repository.TicketRepository
	Event  *repository.EventRepository
	Notify *NotificationService
}

func (s *RegistrationService) RegisterForEvent(userID, eventID, ticketID string) (dto.RegistrationDto, error) {
	if s.Repo.ExistsByEventAndAttendee(eventID, userID) {
		return dto.RegistrationDto{}, &exception.DuplicateRegistrationException{AttendeeID: userID, EventID: eventID}
	}

	event, err := s.Event.FindByEventID(eventID)
	if err != nil {
		return dto.RegistrationDto{}, &exception.EventNotFoundException{ID: eventID}
	}

	reg := model.Registration{
		AttendeeID: userID,
		EventID:    eventID,
		TicketID:   ticketID,
		Status:     data.RegPending,
	}

	s.Repo.DB.Create(&reg)
	s.Notify.SendNotification(userID, "Registration pending for "+event.Name, "EVENT")

	return mapper.ToRegistrationDto(reg), nil
}

func (s *RegistrationService) ApproveRegistration(actorID, regID string) error {
	var reg model.Registration
	if err := s.Repo.DB.First(&reg, "registration_id = ?", regID).Error; err != nil {
		return &exception.RegistrationNotFoundException{Msg: "Registration not found"}
	}
	
	reg.Status = data.RegConfirmed
	s.Repo.DB.Save(&reg)
	s.Notify.SendNotification(reg.AttendeeID, "Registration confirmed", "EVENT")
	return nil
}