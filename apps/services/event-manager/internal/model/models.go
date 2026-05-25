// internal/model/models.go
package model

import (
	"time"
	"github.com/shopspring/decimal" // Use decimal for money
	"eventsphere/event-manager/internal/model/data"
)

type Event struct {
	EventID     string           `gorm:"primaryKey;type:char(36)"`
	Name        string           `gorm:"size:150;not null"`
	OrganizerID string           `gorm:"type:char(36);not null"`
	StartDate   time.Time        `gorm:"not null"`
	EndDate     time.Time        `gorm:"not null"`
	VenueID     string           `gorm:"type:char(36)"`
	Status      data.EventStatus `gorm:"type:varchar(20)"`
	CreatedAt   time.Time        `gorm:"autoCreateTime"`
	UpdatedAt   time.Time        `gorm:"autoUpdateTime"`
}

type Registration struct {
	RegistrationID string                  `gorm:"primaryKey;type:char(36)"`
	EventID        string                  `gorm:"not null;uniqueIndex:idx_event_attendee"`
	Event          Event                   `gorm:"foreignKey:EventID"`
	AttendeeID     string                  `gorm:"not null;uniqueIndex:idx_event_attendee"`
	TicketID       string                  
	Ticket         Ticket                  `gorm:"foreignKey:TicketID"`
	Date           time.Time
	Status         data.RegistrationStatus `gorm:"type:varchar(20)"`
	CreatedAt      time.Time               `gorm:"autoCreateTime"`
	UpdatedAt      time.Time               `gorm:"autoUpdateTime"`
}

type Schedule struct {
	ScheduleID string              `gorm:"primaryKey;type:char(36)"`
	EventID    string              `gorm:"not null"`
	Event      Event               `gorm:"foreignKey:EventID"`
	Date       time.Time           `gorm:"not null"`
	TimeSlot   string              `gorm:"type:char(36);not null"`
	Activity   string              `gorm:"type:char(100);not null"`
	Status     data.ScheduleStatus `gorm:"type:varchar(20)"`
	CreatedAt  time.Time           `gorm:"autoCreateTime"`
	UpdatedAt  time.Time           `gorm:"autoUpdateTime"`
}

type Ticket struct {
	TicketID  string            `gorm:"primaryKey;type:char(36)"`
	EventID   string            `gorm:"not null"`
	Event     Event             `gorm:"foreignKey:EventID"`
	Type      string
	Price     decimal.Decimal   `gorm:"type:decimal(19,2)"`
	Status    data.TicketStatus `gorm:"type:varchar(20)"`
	CreatedAt time.Time         `gorm:"autoCreateTime"`
	UpdatedAt time.Time         `gorm:"autoUpdateTime"`
}