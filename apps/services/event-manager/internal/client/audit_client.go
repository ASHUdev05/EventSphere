package client

import (
	"eventsphere/event-manager/internal/dto"
	"github.com/gin-gonic/gin"
	"github.com/go-resty/resty/v2"
	"github.com/hudl/fargo"
)

type AuditClient struct {
	Conn fargo.EurekaConnection
}

func (c *AuditClient) CreateAudit(ctx *gin.Context, data dto.AuditLogRequestDTO) {
	url, err := GetServiceURL(c.Conn, "AUDIT-MANAGER")
	if err != nil { return }
	
	go func() {
		_, _ = resty.New().R().
			SetHeader("Authorization", ctx.GetHeader("Authorization")).
			SetBody(data).
			Post(url + "/audits")
	}()
}