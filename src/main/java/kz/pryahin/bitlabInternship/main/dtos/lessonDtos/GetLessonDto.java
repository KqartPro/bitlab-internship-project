package kz.pryahin.bitlabInternship.main.dtos.lessonDtos;

import kz.pryahin.bitlabInternship.main.entities.Chapter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetLessonDto {
  private Long id;

  private String name;

  private String description;

  private String content;

  private int lessonOrder;

  private LocalDateTime createdTime;

  private LocalDateTime updatedTime;

  private Chapter chapter;
}
