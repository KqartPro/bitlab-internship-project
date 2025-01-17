package kz.pryahin.bitlabInternship.main.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")

@Data
public class Lesson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  private String content;

  @Column(nullable = false)
  private int lessonOrder;

  @CreationTimestamp
  private LocalDateTime createdTime;

  @UpdateTimestamp
  private LocalDateTime updatedTime;

  @ManyToOne(fetch = FetchType.EAGER)
  private Chapter chapter;


}
