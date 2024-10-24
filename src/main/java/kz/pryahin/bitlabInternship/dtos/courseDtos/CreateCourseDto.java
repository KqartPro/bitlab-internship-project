package kz.pryahin.bitlabInternship.dtos.courseDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCourseDto {
	@NotBlank
	private String name;

	private String description;
}
