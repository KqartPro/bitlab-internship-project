package kz.pryahin.bitlabInternship.main.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chapters")

@Data
public class Chapter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  @CreationTimestamp
  private LocalDateTime createdTime;

  @UpdateTimestamp
  private LocalDateTime updatedTime;

  @Column(nullable = false)
  private int chapterOrder;

  @ManyToOne(fetch = FetchType.EAGER)
  private Course course;
}
