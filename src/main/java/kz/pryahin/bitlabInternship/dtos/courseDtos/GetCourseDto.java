package kz.pryahin.bitlabInternship.dtos.courseDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetCourseDto {
	private Long id;

	private String name;

	private String description;

	private LocalDateTime createdTime;

	private LocalDateTime updatedTime;
}
