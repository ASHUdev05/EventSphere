package controller

import (
	"eventsphere/event-manager/internal/dto"
	"eventsphere/event-manager/internal/service"
	"net/http"
	"github.com/gin-gonic/gin"
)

type EventController struct {
	Service *service.EventService
}

func (ctl *EventController) Create(c *gin.Context) {
	var req dto.EventRequestDto
	if err := c.ShouldBindJSON(&req); err != nil {
		c.Error(err)
		return
	}
	userID := c.GetString("userID") // Populated by your JWT Middleware
	c.JSON(http.StatusCreated, ctl.Service.Create(userID, req))
}

func (ctl *EventController) GetById(c *gin.Context) {
	id := c.Param("id")
	userID := c.GetString("userID")
	resp, err := ctl.Service.FindById(id, userID)
	if err != nil {
		c.Error(err)
		return
	}
	c.JSON(http.StatusOK, resp)
}
// ... Add Update and Delete similarly