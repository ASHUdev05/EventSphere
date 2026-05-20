using Microsoft.EntityFrameworkCore;
using NotificationManager.Data;
using NotificationManager.Models;

namespace NotificationManager.Repositories.Impl;

public class NotificationRepository : INotificationRepository
{
  private readonly AppDbContext _context;

  public NotificationRepository(AppDbContext context) => _context = context;

  public async Task<Notification?> GetByIdAsync(string id) =>
      await _context.Notifications.FindAsync(id);

  public async Task<IEnumerable<Notification>> GetTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDescAsync(string userId, DateTime createdAt)
  {
    return await _context.Notifications
        .Where(n => n.UserId == userId && n.CreatedAt < createdAt)
        .OrderByDescending(n => n.CreatedAt)
        .Take(20)
        .ToListAsync();
  }

  public async Task AddAsync(Notification notification)
  {
    await _context.Notifications.AddAsync(notification);
    await _context.SaveChangesAsync();
  }

  public async Task UpdateAsync(Notification notification)
  {
    _context.Notifications.Update(notification);
    await _context.SaveChangesAsync();
  }
}
