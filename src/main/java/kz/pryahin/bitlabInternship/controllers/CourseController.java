package kz.pryahin.bitlabInternship.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
	private final CourseService courseService;


	@Operation(summary = "Возвращает все курсы из БД")
	@GetMapping("/get-all")
	public ResponseEntity<List<GetCourseDto>> getAllCourses() {

		return ResponseEntity.ok(courseService.getAllCourses());
	}


	@Operation(summary = "Возвращает курс по Id")
	@GetMapping("/get-one/{id}")
	public ResponseEntity<GetCourseDto> getCourseById(@PathVariable Long id) {

		return ResponseEntity.ok(courseService.getCourseById(id));
	}


	@Operation(summary = "Создает курс")
	@PostMapping("/create")
	public ResponseEntity<GetCourseDto> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {

		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(createCourseDto));

	}


	@Operation(summary = "Обновляет курс по Id")
	@PatchMapping("/update/{id}")
	public ResponseEntity<GetCourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody UpdateCourseDto updateCourseDto) {

		return ResponseEntity.ok(courseService.updateCourse(id, updateCourseDto));
	}


	@Operation(summary = "Удаляет курс по Id")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {

		courseService.deleteCourse(id);
		return ResponseEntity.ok().build();
	}

}
