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
	userID := c.GetString("userID")
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

func (ctl *EventController) Update(c *gin.Context) {
	c.JSON(http.StatusNotImplemented, gin.H{"error": "update not implemented"})
}

func (ctl *EventController) Delete(c *gin.Context) {
	c.JSON(http.StatusNotImplemented, gin.H{"error": "delete not implemented"})
}

func (ctl *EventController) CreateActivity(c *gin.Context) {
	c.JSON(http.StatusNotImplemented, gin.H{"error": "create activity not implemented"})
}

func (ctl *EventController) GetAllActivity(c *gin.Context) {
	c.JSON(http.StatusNotImplemented, gin.H{"error": "get all activity not implemented"})
}
