using NotificationManager.Enums;

namespace NotificationManager.Dto;

public record NotificationResponseDto(
    string Id,
    string UserId,
    string? EventId,
    string Message,
    NotificationCategory Category,
    NotificationStatus Status,
    DateTime CreatedDate
);
