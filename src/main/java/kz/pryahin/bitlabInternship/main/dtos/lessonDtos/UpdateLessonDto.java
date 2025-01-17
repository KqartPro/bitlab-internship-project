package kz.pryahin.bitlabInternship.main.dtos.lessonDtos;

import lombok.Data;

@Data
public class UpdateLessonDto {

  private String name;

  private String description;

  private String content;

  private Long chapterId;
}
