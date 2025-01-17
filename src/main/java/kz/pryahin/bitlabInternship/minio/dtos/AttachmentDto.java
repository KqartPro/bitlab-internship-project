package kz.pryahin.bitlabInternship.minio.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentDto {

  private Long id;

  private String name;

  private String url;

  private Long lessonId;

  private LocalDateTime createdTime;
}
