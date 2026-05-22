using Microsoft.EntityFrameworkCore;
using NotificationManager.Models;
using NotificationManager.Enums;

namespace NotificationManager.Data;

public class AppDbContext : DbContext
{
  public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
  {
  }

  public DbSet<Notification> Notifications { get; set; } = null!;

  protected override void OnModelCreating(ModelBuilder modelBuilder)
  {
    base.OnModelCreating(modelBuilder);

    // Convert Category Enum to text strings natively in PostgreSQL
    modelBuilder.Entity<Notification>()
        .Property(n => n.Category)
        .HasConversion(
            v => v.ToString(),
            v => (NotificationCategory)Enum.Parse(typeof(NotificationCategory), v)
        )
        .HasMaxLength(20);

    // Convert Status Enum to text strings natively in PostgreSQL
    modelBuilder.Entity<Notification>()
        .Property(n => n.Status)
        .HasConversion(
            v => v.ToString(),
            v => (NotificationStatus)Enum.Parse(typeof(NotificationStatus), v)
        )
        .HasMaxLength(20);
  }
}
