package kz.pryahin.bitlabInternship.main.dtos.chapterDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChapterDto {

  @NotBlank
  private String name;

  private String description;
}
