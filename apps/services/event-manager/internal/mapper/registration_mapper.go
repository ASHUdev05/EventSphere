package mapper

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/model"
)

func ToRegistrationDto(r model.Registration) dto.RegistrationDto {
	return dto.RegistrationDto{
		RegistrationID: r.RegistrationID,
		EventID:        r.EventID,
		AttendeeID:     r.AttendeeID,
		Status:         string(r.Status),
	}
}