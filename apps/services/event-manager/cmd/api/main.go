package main

import (
	"log"
	"os"

	"eventsphere/event-manager/internal/client"
	"eventsphere/event-manager/internal/controller"
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

	// DB Setup
	db, err := gorm.Open(postgres.Open(os.Getenv("DATABASE_URL")), &gorm.Config{})
	if err != nil {
		log.Fatalf("failed to connect database: %v", err)
	}

	// Eureka Setup
	eurekaConn := fargo.NewConn(os.Getenv("EUREKA_URL"))

	// 1. Initialize Repositories
	eventRepo := repository.NewEventRepository(db)
	regRepo := repository.NewRegistrationRepository(db)
	ticketRepo := repository.NewTicketRepository(db)
	scheduleRepo := repository.NewScheduleRepository(db)

	// 2. Initialize Shared Services
	auditSvc := &service.AuditService{Client: &client.AuditClient{Conn: eurekaConn}}
	notifySvc := &service.NotificationService{Client: &client.LogClient{Conn: eurekaConn}}

	// 3. Initialize Domain Services
	eventSvc := &service.EventService{Repo: eventRepo, Audit: auditSvc, Notify: notifySvc}
	regSvc := &service.RegistrationService{Repo: regRepo, Ticket: ticketRepo, Event: eventRepo, Notify: notifySvc}
	ticketSvc := &service.TicketService{Repo: ticketRepo, Event: eventRepo}
	scheduleSvc := &service.ScheduleService{Repo: scheduleRepo, Event: eventRepo, Notify: notifySvc}

	// 4. Initialize Controllers
	eventCtrl := &controller.EventController{Service: eventSvc}
	regCtrl := &controller.RegistrationController{Service: regSvc}
	ticketCtrl := &controller.TicketController{Service: ticketSvc}
	schedCtrl := &controller.ScheduleController{Service: scheduleSvc}

	// 5. Router Setup
	r := gin.Default()
	r.Use(middleware.GlobalErrorHandler())

	// Event Routes
	r.POST("/events", eventCtrl.Create)
	r.GET("/events/:id", eventCtrl.GetById)
	r.PUT("/events/:id", eventCtrl.Update)
	r.DELETE("/events/:id", eventCtrl.Delete)
	r.POST("/events/:id/schedules", eventCtrl.CreateActivity)
	r.GET("/events/:id/schedules", eventCtrl.GetAllActivity)

	// Registration Routes
	r.POST("/events/:eventId/registrations", regCtrl.CreateRegistration)
	r.PATCH("/registrations/:registrationId/approve", regCtrl.Approve)
	r.PATCH("/registrations/:registrationId/check-in", regCtrl.CheckIn)
	r.PATCH("/registrations/:registrationId/reject", regCtrl.Reject)

	// Ticket Routes
	r.POST("/events/:eventId/tickets", ticketCtrl.CreateTicket)
	r.GET("/events/:eventId/tickets", ticketCtrl.GetTicketsByEventId)
	r.PUT("/tickets/:ticketId", ticketCtrl.UpdateTicket)

	// Schedule Routes
	r.PUT("/events/:eventId/schedules/:id", schedCtrl.Update)
	r.DELETE("/events/:eventId/schedules/:id", schedCtrl.Delete)

	r.Run(":" + portStr)
}
