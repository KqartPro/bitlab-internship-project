package kz.pryahin.bitlabInternship.main.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.UpdateLessonDto;
import kz.pryahin.bitlabInternship.main.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonController {
  private final LessonService lessonService;


  @Operation(summary = "Возвращает все уроки из БД")
  @GetMapping("/get-all")
  public ResponseEntity<List<GetLessonDto>> getAllLessons() {
    return ResponseEntity.ok(lessonService.getAllLessons());
  }


  @Operation(summary = "Возвращает все уроки из курса, сортируя по полю lessonOrder")
  @GetMapping("/get-all-from-chapter")
  public ResponseEntity<List<GetLessonDto>> getAllLessonsFromChapter(@RequestParam Long courseId,
                                                                     @RequestParam Long chapterId) {
    return ResponseEntity.ok(lessonService.getAllLessonsFromChapter(courseId, chapterId));
  }


  @Operation(summary = "Возвращает конкретный урок по Id")
  @GetMapping("/get-one-lesson")
  public ResponseEntity<GetLessonDto> getLessonById(@RequestParam Long courseId,
                                                    @RequestParam Long chapterId,
                                                    @RequestParam Long lessonId) {
    return ResponseEntity.ok(lessonService.getLessonById(courseId, chapterId, lessonId));
  }


  @Operation(summary = "Создает урок в конкретной главе")
  @PostMapping
  public ResponseEntity<GetLessonDto> createLesson(@RequestParam Long courseId,
                                                   @RequestParam Long chapterId,
                                                   @Valid @RequestBody CreateLessonDto createLessonDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(lessonService.createLesson(courseId, chapterId, createLessonDto));
  }


  @Operation(summary = "Обновляет урок в конкретной главе")
  @PatchMapping
  public ResponseEntity<GetLessonDto> updateLesson(@RequestParam Long courseId,
                                                   @RequestParam Long chapterId,
                                                   @RequestParam Long lessonId,
                                                   @Valid @RequestBody UpdateLessonDto updateLessonDto) {
    return ResponseEntity.ok(lessonService.updateLesson(courseId, chapterId, lessonId, updateLessonDto));
  }


  @Operation(summary = "Обновляет поле lessonOrder, отвечающее за порядок отображения уроков")
  @PutMapping("/update-order")
  public ResponseEntity<GetLessonDto> updateLessonOrder(
      @RequestParam Long courseId,
      @RequestParam Long chapterId,
      @RequestParam Long lessonId,
      @RequestParam int lessonOrder) {
    return ResponseEntity.ok(lessonService.updateLessonOrder(courseId, chapterId, lessonId, lessonOrder));
  }


  @Operation(summary = "Удаляет главу в конкретном курсе")
  @DeleteMapping()
  public ResponseEntity<Void> deleteLesson(@RequestParam Long courseId,
                                           @RequestParam Long chapterId,
                                           @RequestParam Long lessonId) {
    lessonService.deleteLesson(courseId, chapterId, lessonId);
    return ResponseEntity.ok().build();
  }

}
