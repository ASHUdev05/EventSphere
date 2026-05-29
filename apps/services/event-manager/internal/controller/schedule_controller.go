package controller

import (
	"eventsphere/event-manager/internal/service"
	"net/http"

	"github.com/gin-gonic/gin"
)

type ScheduleController struct {
	Service *service.ScheduleService
}

func (ctl *ScheduleController) Update(c *gin.Context) {
	eventID := c.Param("eventId")
	scheduleID := c.Param("id")
	if err := ctl.Service.Update(eventID, scheduleID); err != nil {
		c.Error(err)
		return
	}
	c.Status(http.StatusOK)
}

func (ctl *ScheduleController) Delete(c *gin.Context) {
	eventID := c.Param("eventId")
	scheduleID := c.Param("id")
	if err := ctl.Service.Delete(eventID, scheduleID); err != nil {
		c.Error(err)
		return
	}
	c.Status(http.StatusNoContent)
}
