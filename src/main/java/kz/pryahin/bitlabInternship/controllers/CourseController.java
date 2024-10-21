package kz.pryahin.bitlabInternship.controllers;

import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	private final CourseService courseService;


	@GetMapping
	public ResponseEntity<List<GetCourseDto>> getAllCourses() {
		return ResponseEntity.ok(courseService.getAllCourses());
	}


	@GetMapping("/{id}")
	public ResponseEntity<GetCourseDto> getCourseById(@PathVariable Long id) {
		return ResponseEntity.ok(courseService.getCourseById(id));
	}


	@PostMapping
	public ResponseEntity<GetCourseDto> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(createCourseDto));
	}


	@PatchMapping("/{id}")
	public ResponseEntity<GetCourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody UpdateCourseDto updateCourseDto) {
		return ResponseEntity.ok(courseService.updateCourse(id, updateCourseDto));
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
		courseService.deleteCourse(id);
		return ResponseEntity.ok().build();
	}

}
