using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using NotificationManager.Models;
using NotificationManager.Services;

namespace NotificationManager.Controllers;

[ApiController]
[Route("notifications")]
[Authorize]
public class NotificationController : ControllerBase
{
  private readonly INotificationService _notificationService;
  private readonly ILogger<NotificationController> _logger;

  public NotificationController(INotificationService notificationService, ILogger<NotificationController> logger)
  {
    _notificationService = notificationService;
    _logger = logger;
  }

  /// <summary>
  /// Retrieves notifications for a user using infinite scroll pagination.
  /// GET /notifications/{userId}/scroll?limit=20&lastTimestamp=2026-03-26T10:15:30
  /// </summary>
  [HttpGet("{userId}/scroll")]
  public async Task<ActionResult<IEnumerable<Notification>>> GetNotificationsScroll(
      [FromRoute] string userId,
      [FromQuery] DateTime? lastTimestamp,
      [FromQuery] int limit = 20)
  {
    _logger.LogInformation("Fetching notifications for user: {UserId} with limit: {Limit} and lastTimestamp: {LastTimestamp}",
        userId, limit, lastTimestamp);

    var notifications = await _notificationService.GetNotificationsScrollAsync(userId, lastTimestamp, limit);

    _logger.LogInformation("Retrieved {Count} notifications for user: {UserId}", notifications.Count(), userId);
    return Ok(notifications);
  }

  /// <summary>
  /// Sends a new notification to a user (In-App + Email).
  /// POST /notifications/send?userId=...&message=...&category=...
  /// </summary>
  [HttpPost("send")]
  public async Task<IActionResult> SendNotification(
      [FromQuery] string userId,
      [FromQuery] string message,
      [FromQuery] string category)
  {
    _logger.LogInformation("Request to send notification to user: {UserId} (Category: {Category})", userId, category);

    await _notificationService.SendNotificationAsync(userId, message, category);

    _logger.LogInformation("Notification sent successfully to user: {UserId}", userId);
    return StatusCode(StatusCodes.Status201Created);
  }

  /// <summary>
  /// Marks a specific notification as read.
  /// PATCH /notifications/{notificationId}/read
  /// </summary>
  [HttpPatch("{notificationId}/read")]
  public async Task<IActionResult> MarkAsRead([FromRoute] string notificationId)
  {
    _logger.LogInformation("Request to mark notification {NotificationId} as read", notificationId);

    await _notificationService.MarkAsReadAsync(notificationId);

    _logger.LogInformation("Notification {NotificationId} marked as read", notificationId);
    return NoContent();
  }
}
