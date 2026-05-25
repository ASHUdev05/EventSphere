// internal/mapper/event_mapper.go
package mapper

import (
    "eventsphere/event-manager/internal/dto"
    "eventsphere/event-manager/internal/model"
    "time"
)

func ToEventEntity(d dto.EventRequestDto) model.Event {
    start, _ := time.Parse("2006-01-02", d.StartDate)
    end, _ := time.Parse("2006-01-02", d.EndDate)
    
    return model.Event{
        Name:      d.Name,
        StartDate: start,
        EndDate:   end,
        VenueID:   d.VenueID,
    }
}

func ToEventResponse(e model.Event) dto.EventResponseDto {
    return dto.EventResponseDto{
        ID:        e.EventID,
        EventName: e.Name,
        StartAt:   e.StartDate.Format("2006-01-02"),
        EndAt:     e.EndDate.Format("2006-01-02"),
    }
}