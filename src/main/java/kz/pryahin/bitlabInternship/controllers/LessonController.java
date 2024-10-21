package kz.pryahin.bitlabInternship.controllers;

import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.UpdateLessonDto;
import kz.pryahin.bitlabInternship.services.LessonService;
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


	@GetMapping("/get-all")
	public ResponseEntity<List<GetLessonDto>> getAllLessons() {
		return ResponseEntity.ok(lessonService.getAllLessons());
	}


	@GetMapping("/get-all-from-chapter")
	public ResponseEntity<List<GetLessonDto>> getAllLessonsFromChapter(@RequestParam Long courseId,
	                                                                   @RequestParam Long chapterId) {
		return ResponseEntity.ok(lessonService.getAllLessonsFromChapter(courseId, chapterId));
	}


	@GetMapping("/get-one-lesson")
	public ResponseEntity<GetLessonDto> getLessonById(@RequestParam Long courseId,
	                                                  @RequestParam Long chapterId,
	                                                  @RequestParam Long lessonId) {
		return ResponseEntity.ok(lessonService.getLessonById(courseId, chapterId, lessonId));
	}


	@PostMapping
	public ResponseEntity<GetLessonDto> createLesson(@RequestParam Long courseId,
	                                                 @RequestParam Long chapterId,
	                                                 @Valid @RequestBody CreateLessonDto createLessonDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLesson(courseId, chapterId, createLessonDto));
	}


	@PatchMapping
	public ResponseEntity<GetLessonDto> updateLesson(@RequestParam Long courseId,
	                                                 @RequestParam Long chapterId,
	                                                 @RequestParam Long lessonId,
	                                                 @Valid @RequestBody UpdateLessonDto updateLessonDto) {
		return ResponseEntity.ok(lessonService.updateLesson(courseId, chapterId, lessonId, updateLessonDto));
	}


	@PutMapping("/update-order")
	public ResponseEntity<GetLessonDto> updateLessonOrder(
		@RequestParam Long courseId,
		@RequestParam Long chapterId,
		@RequestParam Long lessonId,
		@RequestParam int lessonOrder) {
		return ResponseEntity.ok(lessonService.updateLessonOrder(courseId, chapterId, lessonId, lessonOrder));
	}


	@DeleteMapping()
	public ResponseEntity<Void> deleteLesson(@RequestParam Long courseId,
	                                         @RequestParam Long chapterId,
	                                         @RequestParam Long lessonId) {
		lessonService.deleteLesson(courseId, chapterId, lessonId);
		return ResponseEntity.ok().build();
	}

}
