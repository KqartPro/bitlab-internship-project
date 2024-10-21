package kz.pryahin.bitlabInternship.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")

@Data
public class Course {
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
}
