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
	eventID := c.Param("eventId")
	var req dto.CreateTicketRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.Error(err)
		return
	}
	actorID := c.GetString("userID")
	resp, err := ctl.Service.CreateTicket(actorID, eventID, req.Type, req.Price, req.Status)
	if err != nil {
		c.Error(err)
		return
	}
	c.JSON(http.StatusOK, resp)
}

func (ctl *TicketController) GetTicketsByEventId(c *gin.Context) {
	c.JSON(http.StatusNotImplemented, gin.H{"error": "get tickets not implemented"})
}

func (ctl *TicketController) UpdateTicket(c *gin.Context) {
	c.JSON(http.StatusNotImplemented, gin.H{"error": "update ticket not implemented"})
}
