package service

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/exception"
	"eventsphere/event-manager/internal/mapper"
	"eventsphere/event-manager/internal/model"
	"eventsphere/event-manager/internal/model/data"
	"eventsphere/event-manager/internal/repository"
	"github.com/shopspring/decimal"
)

type TicketService struct {
	Repo  *repository.TicketRepository
	Event *repository.EventRepository
}

func (s *TicketService) CreateTicket(actorID, eventID, tType string, price float64, status data.TicketStatus) (dto.TicketResponseDto, error) {
	_, err := s.Event.FindByEventID(eventID)
	if err != nil {
		return dto.TicketResponseDto{}, &exception.EventNotFoundException{ID: eventID}
	}

	existing, _ := s.Repo.FindByEventIDAndType(eventID, tType)
	if existing.TicketID != "" {
		return dto.TicketResponseDto{}, &exception.TicketAlreadyExistsException{Msg: "Ticket type exists"}
	}

	ticket := model.Ticket{
		EventID: eventID,
		Type:    tType,
		Price:   decimal.NewFromFloat(price),
		Status:  status,
	}

	s.Repo.DB.Create(&ticket)
	return mapper.ToTicketDto(ticket), nil
}