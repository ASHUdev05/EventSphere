package service

import "eventsphere/event-manager/internal/client"

type NotificationService struct {
	Client *client.LogClient
}

func (s *NotificationService) SendNotification(userID, message, category string) {
	s.Client.SendNotification(userID, message, category)
}