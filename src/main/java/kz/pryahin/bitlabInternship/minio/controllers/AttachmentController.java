package kz.pryahin.bitlabInternship.minio.controllers;

import io.swagger.v3.oas.annotations.Operation;
import kz.pryahin.bitlabInternship.minio.dtos.AttachmentDto;
import kz.pryahin.bitlabInternship.minio.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class AttachmentController {
    private final AttachmentService attachmentService;


    @Operation(summary = "Возвращает все вложения")
    @GetMapping("/get-all")
    public List<AttachmentDto> getAllAttachments() {
        return attachmentService.getAllAttachments();
    }


    @Operation(summary = "Возвращает все вложения определенного курса")
    @GetMapping("/get-all-lesson-attachments/{lesson-id}")
    public List<AttachmentDto> getAllAttachmentsByLessonId(@PathVariable("lesson-id") Long lessonId) {
        return attachmentService.getAllAttachmentsByLessonId(lessonId);
    }


    @Operation(summary = "Загружает файл в minio")
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("lessonId") Long lessonId) {
        return attachmentService.uploadFile(file, lessonId);
    }


    @Operation(summary = "Скачивает файл из minio")
    @GetMapping("/download/{file-name}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("file-name") String fileName) {
        if (attachmentService.downloadFile(fileName) == null) {
            throw new NotFoundException("File not found");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(attachmentService.downloadFile(fileName));
    }
}
