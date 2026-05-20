using NotificationManager.Enums;

namespace NotificationManager.Dto;

public record NotificationRequestDto(
    string UserId,
    string? EventId,
    string Message,
    NotificationCategory Category
);
