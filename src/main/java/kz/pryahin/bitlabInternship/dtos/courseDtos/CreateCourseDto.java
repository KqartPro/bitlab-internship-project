package kz.pryahin.bitlabInternship.dtos.courseDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateCourseDto {
	@NotBlank
	private String name;

	private String description;
}
