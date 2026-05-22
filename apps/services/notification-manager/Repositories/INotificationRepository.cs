using NotificationManager.Models;

namespace NotificationManager.Repositories;

public interface INotificationRepository
{
  Task<Notification?> GetByIdAsync(string id);
  Task<IEnumerable<Notification>> GetTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDescAsync(string userId, DateTime createdAt);
  Task AddAsync(Notification notification);
  Task UpdateAsync(Notification notification);
}
