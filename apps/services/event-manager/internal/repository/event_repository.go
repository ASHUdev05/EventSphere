package repository

import (
	"eventsphere/event-manager/internal/model"
	"gorm.io/gorm"
)

type EventRepository struct { DB *gorm.DB }

func NewEventRepository(db *gorm.DB) *EventRepository { return &EventRepository{DB: db} }

func (r *EventRepository) FindByEventID(id string) (*model.Event, error) {
	var e model.Event
	err := r.DB.Where("event_id = ?", id).First(&e).Error
	return &e, err
}