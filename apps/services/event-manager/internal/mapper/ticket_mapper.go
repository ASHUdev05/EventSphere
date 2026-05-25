package mapper

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/model"
)

func ToTicketDto(t model.Ticket) dto.TicketResponseDto {
	price, _ := t.Price.Float64()
	return dto.TicketResponseDto{
		TicketID: t.TicketID,
		EventID:  t.EventID,
		Type:     t.Type,
		Price:    price,
		Status:   t.Status,
	}
}