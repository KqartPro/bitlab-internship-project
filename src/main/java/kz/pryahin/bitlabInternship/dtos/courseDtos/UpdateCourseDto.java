package kz.pryahin.bitlabInternship.dtos.courseDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCourseDto {
	private String name;
	private String description;
}
