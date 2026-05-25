package dto

type RegistrationDto struct {
    RegistrationID string `json:"registrationId"`
    EventID        string `json:"eventId"`
    TicketID       string `json:"ticketId"`
    AttendeeID     string `json:"attendeeId"`
    Status         string `json:"status"`
}