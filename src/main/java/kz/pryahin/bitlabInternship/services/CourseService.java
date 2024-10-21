package kz.pryahin.bitlabInternship.services;

import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.UpdateCourseDto;

import java.util.List;

public interface CourseService {
	List<GetCourseDto> getAllCourses();

	GetCourseDto getCourseById(Long id);

	GetCourseDto createCourse(CreateCourseDto courseDto);

	GetCourseDto updateCourse(Long id, UpdateCourseDto courseDto);

	void deleteCourse(Long id);
}
