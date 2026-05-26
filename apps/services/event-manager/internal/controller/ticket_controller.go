package controller

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/service"
	"net/http"
	"github.com/gin-gonic/gin"
)

type TicketController struct {
	Service *service.TicketService
}

func (ctl *TicketController) CreateTicket(c *gin.Context) {
	eventId := c.Param("eventId")
	var req dto.CreateTicketRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.Error(err)
		return
	}
	actorID := c.GetString("userID")
	c.JSON(http.StatusOK, ctl.Service.CreateTicket(actorID, eventId, req.Type, req.Price, req.Status))
}