package data

type EventStatus string
const (
	EventDraft     EventStatus = "DRAFT"
	EventPublished EventStatus = "PUBLISHED"
	EventCompleted EventStatus = "COMPLETED"
	EventCancelled EventStatus = "CANCELLED"
)

type RegistrationStatus string
const (
	RegPending   RegistrationStatus = "PENDING"
	RegConfirmed RegistrationStatus = "CONFIRMED"
	RegCancelled RegistrationStatus = "CANCELLED"
	RegCheckedIn RegistrationStatus = "CHECKED_IN"
)

type ScheduleStatus string
const (
	ScheduleDraft     ScheduleStatus = "DRAFT"
	ScheduleActive    ScheduleStatus = "ACTIVE"
	ScheduleCompleted ScheduleStatus = "COMPLETED"
	ScheduleTerminated ScheduleStatus = "TERMINATED"
)

type TicketStatus string
const (
	TicketActive   TicketStatus = "ACTIVE"
	TicketInactive TicketStatus = "INACTIVE"
)