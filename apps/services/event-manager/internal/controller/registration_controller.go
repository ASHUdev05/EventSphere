package controller

import (
	"eventsphere/event-manager/internal/service"
	"net/http"

	"github.com/gin-gonic/gin"
)

type RegistrationController struct {
	Service *service.RegistrationService
}

func (ctl *RegistrationController) CreateRegistration(c *gin.Context) {
	eventID := c.Param("eventId")
	var req struct {
		TicketID string `json:"ticketId" binding:"required"`
	}
	if err := c.ShouldBindJSON(&req); err != nil {
		c.Error(err)
		return
	}
	actorID := c.GetString("userID")
	resp, err := ctl.Service.RegisterForEvent(actorID, eventID, req.TicketID)
	if err != nil {
		c.Error(err)
		return
	}
	c.JSON(http.StatusCreated, resp)
}

func (ctl *RegistrationController) Approve(c *gin.Context) {
	regID := c.Param("registrationId")
	actorID := c.GetString("userID")
	if err := ctl.Service.ApproveRegistration(actorID, regID); err != nil {
		c.Error(err)
		return
	}
	c.Status(http.StatusOK)
}

func (ctl *RegistrationController) CheckIn(c *gin.Context) {
	regID := c.Param("registrationId")
	actorID := c.GetString("userID")
	if err := ctl.Service.CheckInRegistration(actorID, regID); err != nil {
		c.Error(err)
		return
	}
	c.Status(http.StatusOK)
}

func (ctl *RegistrationController) Reject(c *gin.Context) {
	regID := c.Param("registrationId")
	actorID := c.GetString("userID")
	if err := ctl.Service.RejectRegistration(actorID, regID); err != nil {
		c.Error(err)
		return
	}
	c.Status(http.StatusOK)
}
