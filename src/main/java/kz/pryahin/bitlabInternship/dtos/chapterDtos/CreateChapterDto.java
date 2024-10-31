package kz.pryahin.bitlabInternship.dtos.chapterDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChapterDto {

	@NotBlank
	private String name;

	private String description;
}
