package kz.pryahin.bitlabInternship.minio.services.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.pryahin.bitlabInternship.main.entities.Lesson;
import kz.pryahin.bitlabInternship.main.exceptions.LessonNotFoundException;
import kz.pryahin.bitlabInternship.main.repositories.LessonRepository;
import kz.pryahin.bitlabInternship.minio.dtos.AttachmentDto;
import kz.pryahin.bitlabInternship.minio.entities.Attachment;
import kz.pryahin.bitlabInternship.minio.mappers.AttachmentMapper;
import kz.pryahin.bitlabInternship.minio.repositories.AttachmentRepository;
import kz.pryahin.bitlabInternship.minio.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final MinioClient minioClient;
    private final LessonRepository lessonRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.download-url}")
    private String downloadUrl;


    @Override
    public List<AttachmentDto> getAllAttachments() {
        return attachmentRepository.findAll().stream()
                .map(attachment -> {
                    AttachmentDto dto = attachmentMapper.mapToDto(attachment);

                    if (attachment.getLesson() != null) {
                        dto.setLessonId(attachment.getLesson().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<AttachmentDto> getAllAttachmentsByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(LessonNotFoundException::new);

        return attachmentRepository.findAllByLessonId(lessonId).stream()
                .map(attachment -> {
                    AttachmentDto dto = attachmentMapper.mapToDto(attachment);

                    if (attachment.getLesson() != null) {
                        dto.setLessonId(attachment.getLesson().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public String uploadFile(MultipartFile file, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(LessonNotFoundException::new);

        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setUrl("temp");
        attachment.setLesson(lesson);

        attachment = attachmentRepository.save(attachment);

        String fileName = DigestUtils.sha256Hex(attachment.getId() + "secret") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        attachment.setUrl(downloadUrl + "/" + fileName);


        attachmentRepository.save(attachment);

        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            return "File uploaded";

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

        return "Error on file uploading";

    }


    @Override
    public ByteArrayResource downloadFile(String fileName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();

            InputStream stream = minioClient.getObject(getObjectArgs);
            byte[] byteArray = IOUtils.toByteArray(stream);
            stream.close();

            return new ByteArrayResource(byteArray);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

        return null;
    }
}
