package kz.pryahin.bitlabInternship.dtos.chapterDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChapterDto {

	@NotBlank
	private String name;

	private String description;
}
