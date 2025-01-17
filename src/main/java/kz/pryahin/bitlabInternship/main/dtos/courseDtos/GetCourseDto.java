package kz.pryahin.bitlabInternship.main.dtos.courseDtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetCourseDto {
  private Long id;

  private String name;

  private String description;

  private LocalDateTime createdTime;

  private LocalDateTime updatedTime;
}
