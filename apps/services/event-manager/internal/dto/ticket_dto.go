package dto

type CreateTicketRequest struct {
    Type   string  `json:"type" binding:"required,max=50"`
    Price  float64 `json:"price" binding:"required,gt=0"`
    Status string  `json:"status" binding:"required"`
}