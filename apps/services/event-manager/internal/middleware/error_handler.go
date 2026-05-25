package middleware

import (
	"eventsphere/event-manager/internal/exception"
	"net/http"
	"github.com/gin-gonic/gin"
)

func GlobalErrorHandler() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Next()

		if len(c.Errors) > 0 {
			err := c.Errors.Last().Err

			switch e := err.(type) {
			case *exception.EventNotFoundException, 
				 *exception.TicketNotFoundException, 
				 *exception.ScheduleNotFoundException, 
				 *exception.RegistrationNotFoundException:
				c.JSON(http.StatusNotFound, gin.H{"error": e.Error()})

			case *exception.TicketAlreadyExistsException, 
				 *exception.DuplicateRegistrationException:
				c.JSON(http.StatusConflict, gin.H{"error": e.Error()})

			case *exception.TicketUnavailableException:
				c.JSON(http.StatusUnprocessableEntity, gin.H{"error": e.Error()})

			case *exception.InvalidRegistrationStatusException:
				c.JSON(http.StatusBadRequest, gin.H{"error": e.Error()})

			default:
				c.JSON(http.StatusInternalServerError, gin.H{"error": "Internal server error"})
			}
		}
	}
}