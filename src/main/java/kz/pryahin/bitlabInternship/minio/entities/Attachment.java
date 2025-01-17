package kz.pryahin.bitlabInternship.minio.entities;

import jakarta.persistence.*;
import kz.pryahin.bitlabInternship.main.entities.Lesson;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "attachments")

@Data
public class Attachment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String url;

  @ManyToOne(fetch = FetchType.EAGER)
  private Lesson lesson;

  @CreationTimestamp
  private LocalDateTime createdTime;

}
