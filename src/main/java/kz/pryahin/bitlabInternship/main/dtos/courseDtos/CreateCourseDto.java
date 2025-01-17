package kz.pryahin.bitlabInternship.main.dtos.courseDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateCourseDto {
  @NotBlank
  private String name;

  private String description;
}
