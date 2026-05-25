// internal/dto/audit_dto.go
package dto
import "eventsphere/event-manager/internal/model/data"

type AuditLogRequestDTO struct {
    Action     data.AuditAction `json:"action"`
    EntityID   string           `json:"entityId"`
    EntityName string           `json:"entityName"`
}

// internal/dto/event_dto.go
type EventRequestDto struct {
    Name        string `json:"name" binding:"required,max=100"`
    OrganizerID string `json:"organizerId" binding:"required"`
    StartDate   string `json:"startDate" binding:"required"` // Use string for JSON parsing
    EndDate     string `json:"endDate" binding:"required"`
    VenueID     string `json:"venueId"`
    Status      string `json:"status"`
}

type EventResponseDto struct {
    ID          string `json:"id"`
    EventName   string `json:"eventName"`
    OrganizerID string `json:"organizerId"`
    StartAt     string `json:"startAt"`
    EndAt       string `json:"endAt"`
    Status      string `json:"status"`
    VenueID     string `json:"venueId"`
}