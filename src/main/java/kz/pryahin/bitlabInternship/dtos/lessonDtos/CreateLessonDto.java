package kz.pryahin.bitlabInternship.dtos.lessonDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateLessonDto {

	@NotBlank
	private String name;

	private String description;

	private String content;

}
