package com.nyad.authmanager.model;

import com.nyad.authmanager.model.data.UserRole;
import com.nyad.authmanager.model.data.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String UserID;

  @Column(nullable = false, length = 100)
  private String Name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole Role = UserRole.ATTENDEE;

  @Column(nullable = false, length = 100)
  private String Email;

  @Column(nullable = false, length = 255)
  private String Password;

  @Column(nullable = false, length = 20)
  private String Phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus Status =  UserStatus.ACTIVE;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @CreationTimestamp
  private LocalDateTime updatedAt = LocalDateTime.now();
}
