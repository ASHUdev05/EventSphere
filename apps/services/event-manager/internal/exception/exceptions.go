package exception

import "fmt"

type EventNotFoundException struct { ID string }
func (e *EventNotFoundException) Error() string { return fmt.Sprintf("Event with ID %s not found", e.ID) }

type TicketNotFoundException struct { ID string }
func (e *TicketNotFoundException) Error() string { return fmt.Sprintf("Ticket with id '%s' not found", e.ID) }

type TicketAlreadyExistsException struct { Msg string }
func (e *TicketAlreadyExistsException) Error() string { return e.Msg }

type TicketUnavailableException struct { EventID string }
func (e *TicketUnavailableException) Error() string { return fmt.Sprintf("No available tickets for event with id '%s'", e.EventID) }

type ScheduleNotFoundException struct { ID string }
func (e *ScheduleNotFoundException) Error() string { return fmt.Sprintf("Could not find schedule with ID: %s", e.ID) }

type DuplicateRegistrationException struct { AttendeeID, EventID string }
func (e *DuplicateRegistrationException) Error() string { return fmt.Sprintf("Attendee '%s' is already registered for event '%s'", e.AttendeeID, e.EventID) }

type InvalidRegistrationStatusException struct { Msg string }
func (e *InvalidRegistrationStatusException) Error() string { return e.Msg }

type RegistrationNotFoundException struct { Msg string }
func (e *RegistrationNotFoundException) Error() string { return e.Msg }