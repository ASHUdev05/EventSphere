package dto

import "eventsphere/event-manager/internal/model/data"

type TicketResponseDto struct {
	TicketID string            `json:"ticketId"`
	EventID  string            `json:"eventId"`
	Type     string            `json:"type"`
	Price    float64           `json:"price"`
	Status   data.TicketStatus `json:"status"`
}