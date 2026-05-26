package controller

import (
	"eventsphere/event-manager/internal/service"
	"net/http"
	"github.com/gin-gonic/gin"
)

type RegistrationController struct {
	Service *service.RegistrationService
}

func (ctl *RegistrationController) Approve(c *gin.Context) {
	regID := c.Param("registrationId")
	actorID := c.GetString("userID")
	
	resp := ctl.Service.ApproveRegistration(actorID, regID)
	c.JSON(http.StatusOK, resp)
}

func (ctl *RegistrationController) CheckIn(c *gin.Context) {
	regID := c.Param("registrationId")
	actorID := c.GetString("userID")
	
	c.JSON(http.StatusOK, ctl.Service.CheckInRegistration(actorID, regID))
}