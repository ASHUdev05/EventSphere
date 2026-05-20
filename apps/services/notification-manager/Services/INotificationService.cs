using NotificationManager.Models;

namespace NotificationManager.Services;

public interface INotificationService
{
  Task<IEnumerable<Notification>> GetNotificationsScrollAsync(string userId, DateTime? lastTimestamp, int limit);
  Task SendNotificationAsync(string userId, string message, string category);
  Task MarkAsReadAsync(string notificationId);
}
