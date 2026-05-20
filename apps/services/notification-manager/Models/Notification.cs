using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using NotificationManager.Enums;

namespace NotificationManager.Models;

[Table("notifications")]
public class Notification
{
  [Key]
  [Column("notification_id", TypeName = "CHAR(36)")]
  [StringLength(36)]
  public string NotificationId { get; set; } = Guid.NewGuid().ToString();

  [Required]
  [Column("user_id", TypeName = "CHAR(36)")]
  [StringLength(36)]
  public string UserId { get; set; } = string.Empty;

  [Column("event_id", TypeName = "CHAR(36)")]
  [StringLength(36)]
  public string? EventId { get; set; }

  [Required]
  [Column("message")]
  [StringLength(255)]
  public string Message { get; set; } = string.Empty;

  [Required]
  [Column("category")]
  public NotificationCategory Category { get; set; }

  [Required]
  [Column("status")]
  public NotificationStatus Status { get; set; } = NotificationStatus.UNREAD;

  [Column("created_at")]
  public DateTime CreatedAt { get; set; }

  [Column("updated_at")]
  public DateTime UpdatedAt { get; set; }
}
