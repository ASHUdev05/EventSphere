package client

import (
	"github.com/go-resty/resty/v2"
	"github.com/hudl/fargo"
)

type LogClient struct {
	Conn fargo.EurekaConnection
}

func (c *LogClient) SendNotification(userID, message, category string) {
	url, err := GetServiceURL(c.Conn, "NOTIFICATION-MANAGER")
	if err != nil { return }
	
	go func() {
		_, _ = resty.New().R().
			SetQueryParams(map[string]string{"userId": userID, "message": message, "category": category}).
			Post(url + "/notifications/send")
	}()
}