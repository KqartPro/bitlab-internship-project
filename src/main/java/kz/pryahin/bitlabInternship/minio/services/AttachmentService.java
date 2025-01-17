package kz.pryahin.bitlabInternship.minio.services;


import kz.pryahin.bitlabInternship.minio.dtos.AttachmentDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
  String uploadFile(MultipartFile file, Long lessonId);

  ByteArrayResource downloadFile(String fileName);

  List<AttachmentDto> getAllAttachments();

  List<AttachmentDto> getAllAttachmentsByLessonId(Long lessonId);
}