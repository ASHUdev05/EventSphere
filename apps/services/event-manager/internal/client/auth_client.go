package client

import (
	"github.com/gin-gonic/gin"
	"github.com/go-resty/resty/v2"
)

// GetAuthenticatedClient creates a client that propagates the Authorization header
// from the incoming Gin request to outgoing calls.
func GetAuthenticatedClient(c *gin.Context) *resty.Client {
	client := resty.New()
	
	// Get the token from the incoming request
	token := c.GetHeader("Authorization")
	
	if token != "" {
		client.SetHeader("Authorization", token)
	}
	
	return client
}