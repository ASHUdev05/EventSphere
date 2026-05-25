package repository

import (
	"eventsphere/event-manager/internal/model"
	"eventsphere/event-manager/internal/model/data"
	"gorm.io/gorm"
)

type RegistrationRepository struct {
	DB *gorm.DB
}

func NewRegistrationRepository(db *gorm.DB) *RegistrationRepository {
	return &RegistrationRepository{DB: db}
}

func (r *RegistrationRepository) FindByEventID(eventID string, limit, offset int) ([]model.Registration, error) {
	var regs []model.Registration
	err := r.DB.Where("event_id = ?", eventID).Limit(limit).Offset(offset).Find(&regs).Error
	return regs, err
}

func (r *RegistrationRepository) FindByEventIDAndStatus(eventID string, status data.RegistrationStatus, limit, offset int) ([]model.Registration, error) {
	var regs []model.Registration
	err := r.DB.Where("event_id = ? AND status = ?", eventID, status).Limit(limit).Offset(offset).Find(&regs).Error
	return regs, err
}

func (r *RegistrationRepository) FindByAttendeeID(attendeeID string, limit, offset int) ([]model.Registration, error) {
	var regs []model.Registration
	err := r.DB.Where("attendee_id = ?", attendeeID).Limit(limit).Offset(offset).Find(&regs).Error
	return regs, err
}

func (r *RegistrationRepository) ExistsByEventAndAttendee(eventID, attendeeID string) bool {
	var count int64
	r.DB.Model(&model.Registration{}).Where("event_id = ? AND attendee_id = ?", eventID, attendeeID).Count(&count)
	return count > 0
}

func (r *RegistrationRepository) FindByAttendeeIDAndEventID(attendeeID, eventID string) (*model.Registration, error) {
	var reg model.Registration
	err := r.DB.Where("attendee_id = ? AND event_id = ?", attendeeID, eventID).First(&reg).Error
	return &reg, err
}