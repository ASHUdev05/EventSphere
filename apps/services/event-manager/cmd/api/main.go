package main

import (
  "log"
  "net/http"
  "os"
  "strconv"
  "time"

  "github.com/gin-gonic/gin"
  "github.com/hudl/fargo"
  "github.com/joho/godotenv"
)

func main() {
  err := godotenv.Load()
  if err != nil {
    log.Println("No .env file found, relying on system environment variables")
  }

  portStr := os.Getenv("SERVER_PORT")
  if portStr == "" {
    portStr = "8080"
  }

  // Convert string port to integer for fargo instance
  port, err := strconv.Atoi(portStr)
  if err != nil {
    log.Fatalf("Invalid SERVER_PORT: %v", err)
  }

  eurekaURL := os.Getenv("EUREKA_URL")

  instance := &fargo.Instance{
    InstanceId:     "fedora:event-manager:" + portStr,
    HostName:       "fedora",
    Port:           port,
    App:            "EVENT-MANAGER",
    IPAddr:         "127.0.0.1",
    Status:         fargo.UP,
    DataCenterInfo: fargo.DataCenterInfo{Name: fargo.MyOwn},
  }

  eurekaConn := fargo.NewConn(eurekaURL)

  err = eurekaConn.RegisterInstance(instance)
  if err != nil {
    log.Fatalf("Eureka registration failed: %v", err)
  }

  go func() {
    ticker := time.NewTicker(30 * time.Second)
    for range ticker.C {
      err := eurekaConn.HeartBeatInstance(instance)
      if err != nil {
        log.Printf("Heartbeat failed: %v", err)
      }
    }
  }()

  r := gin.Default()

  r.GET("/health", func(c *gin.Context) {
    c.JSON(http.StatusOK, gin.H{"status": "UP"})
  })

  r.Run(":" + portStr)
}
