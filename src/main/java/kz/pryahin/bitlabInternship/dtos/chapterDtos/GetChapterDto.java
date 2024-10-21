package kz.pryahin.bitlabInternship.dtos.chapterDtos;

import kz.pryahin.bitlabInternship.entities.Course;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetChapterDto {
	private Long id;

	private String name;

	private String description;

	private LocalDateTime createdTime;

	private LocalDateTime updatedTime;

	private int chapterOrder;

	private Course course;
}
