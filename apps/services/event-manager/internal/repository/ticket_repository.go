package repository

import (
	"eventsphere/event-manager/internal/model"
	"gorm.io/gorm"
)

type TicketRepository struct {
	DB *gorm.DB
}

func NewTicketRepository(db *gorm.DB) *TicketRepository {
	return &TicketRepository{DB: db}
}

func (r *TicketRepository) FindByEventID(eventID string, limit, offset int) ([]model.Ticket, error) {
	var tickets []model.Ticket
	err := r.DB.Where("event_id = ?", eventID).Limit(limit).Offset(offset).Find(&tickets).Error
	return tickets, err
}

func (r *TicketRepository) FindByEventIDAndType(eventID, ticketType string) (*model.Ticket, error) {
	var ticket model.Ticket
	err := r.DB.Where("event_id = ? AND type = ?", eventID, ticketType).First(&ticket).Error
	return &ticket, err
}