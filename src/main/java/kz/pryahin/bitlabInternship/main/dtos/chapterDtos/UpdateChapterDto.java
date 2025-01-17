package kz.pryahin.bitlabInternship.main.dtos.chapterDtos;

import lombok.Data;

@Data
public class UpdateChapterDto {
  private String name;

  private String description;

  private Long courseId;
}
