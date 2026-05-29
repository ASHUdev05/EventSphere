package service

import "eventsphere/event-manager/internal/repository"

type ScheduleService struct {
	Repo  *repository.ScheduleRepository
	Event *repository.EventRepository
	Notify *NotificationService
}

func (s *ScheduleService) Update(eventID, scheduleID string) error {
	return nil
}

func (s *ScheduleService) Delete(eventID, scheduleID string) error {
	return nil
}
