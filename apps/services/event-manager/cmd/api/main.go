package main

import (
	"log"
	"os"
	"strconv"

	"eventsphere/event-manager/internal/client"
	"eventsphere/event-manager/internal/middleware"
	"eventsphere/event-manager/internal/repository"
	"eventsphere/event-manager/internal/service"
	"github.com/gin-gonic/gin"
	"github.com/hudl/fargo"
	"github.com/joho/godotenv"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func main() {
	_ = godotenv.Load()
	portStr := os.Getenv("SERVER_PORT")
	port, _ := strconv.Atoi(portStr)

	// DB Setup
	db, err := gorm.Open(postgres.Open(os.Getenv("DATABASE_URL")), &gorm.Config{})
	if err != nil {
		log.Fatalf("failed to connect database: %v", err)
	}

	// Initialize Infrastructure
	eurekaConn := fargo.NewConn(os.Getenv("EUREKA_URL"))
	
	// Wire Layers (Repo -> Service -> Controller)
	eventRepo := repository.NewEventRepository(db)
	auditSvc := &service.AuditService{Client: &client.AuditClient{Conn: eurekaConn}}
	notifySvc := &service.NotificationService{Client: &client.LogClient{Conn: eurekaConn}}
	
	eventSvc := &service.EventService{Repo: eventRepo, Audit: auditSvc, Notify: notifySvc}
	eventCtrl := &controller.EventController{Service: eventSvc}

	// Router Setup
	r := gin.Default()
	r.Use(middleware.GlobalErrorHandler())
	
	r.POST("/events", eventCtrl.Create)
	r.GET("/events/:id", eventCtrl.GetEvent)

	r.Run(":" + portStr)
}