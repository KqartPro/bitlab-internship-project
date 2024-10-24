package kz.pryahin.bitlabInternship.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.UpdateChapterDto;
import kz.pryahin.bitlabInternship.services.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chapter")
public class ChapterController {
	private final ChapterService chapterService;


	@Operation(summary = "Возвращает все главы из БД")
	@GetMapping("/get-all")
	public ResponseEntity<List<GetChapterDto>> getAllChapters() {
		return ResponseEntity.ok(chapterService.getAllChapters());
	}


	@Operation(summary = "Возвращает все главы из курса, сортируя по полю chapterOrder")
	@GetMapping("/get-all-from-course/{courseId}")
	public ResponseEntity<List<GetChapterDto>> getAllChaptersByCourseId(@PathVariable Long courseId) {
		return ResponseEntity.ok(chapterService.getAllChaptersByCourseId(courseId));
	}


	@Operation(summary = "Возвращает конкретную главу по Id")
	@GetMapping("/get-one-chapter")
	public ResponseEntity<GetChapterDto> getChapterById(@RequestParam Long courseId, @RequestParam Long chapterId) {
		return ResponseEntity.ok(chapterService.getChapterById(courseId, chapterId));
	}


	@Operation(summary = "Создает главу в конкретном курсе")
	@PostMapping("/create/{courseId}")
	public ResponseEntity<GetChapterDto> createChapter(@PathVariable Long courseId,
	                                                   @Valid @RequestBody CreateChapterDto createChapterDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.createChapter(courseId, createChapterDto));
	}


	@Operation(summary = "Обновляет главу в конкретном курсе")
	@PatchMapping("/update")
	public ResponseEntity<GetChapterDto> updateChapter(@RequestParam Long courseId,
	                                                   @RequestParam Long chapterId,
	                                                   @Valid @RequestBody UpdateChapterDto updateChapterDto) {
		return ResponseEntity.ok(chapterService.updateChapter(courseId, chapterId, updateChapterDto));
	}


	@Operation(summary = "Обновляет поле chapterOrder, отвечающее за порядок отображения глав")
	@PutMapping("/update-order")
	public ResponseEntity<GetChapterDto> updateChapterOrder(
		@RequestParam Long courseId,
		@RequestParam Long chapterId,
		@RequestParam int chapterOrder) {
		return ResponseEntity.ok(chapterService.updateChapterOrder(courseId, chapterId, chapterOrder));
	}


	@Operation(summary = "Удаляет главу в конкретном курсе")
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteChapter(@RequestParam Long courseId, @RequestParam Long chapterId) {
		chapterService.deleteChapter(courseId, chapterId);
		return ResponseEntity.ok().build();
	}

}
