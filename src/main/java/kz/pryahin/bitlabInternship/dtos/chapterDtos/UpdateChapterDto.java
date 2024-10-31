package kz.pryahin.bitlabInternship.dtos.chapterDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChapterDto {
	private String name;

	private String description;

	private Long courseId;
}
