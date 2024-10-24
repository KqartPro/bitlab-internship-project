package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.entities.Course;
import kz.pryahin.bitlabInternship.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.mapper.CourseMapper;
import kz.pryahin.bitlabInternship.repositories.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
	/* Мы создаем Mock (Имитацию) нашего CourseRepository и инжектируем её в CourseService, так как CourseService не
	 может работать без CourseRepository */

	private Course course1;
	private Course course2;
	private GetCourseDto getCourseDto1;
	private GetCourseDto getCourseDto2;
	private CreateCourseDto createCourseDto1;
	private CreateCourseDto createCourseDto2;
	private UpdateCourseDto updateCourseDto1;
	private UpdateCourseDto updateCourseDto2;


	@BeforeEach
	void setUp() {
		LocalDateTime time = LocalDateTime.now();
		course1 = new Course(1L, "Course1", "Desc1", time, time);
		course2 = new Course(2L, "Course2", "Desc2", time, time);
		getCourseDto1 = new GetCourseDto(1L, "Course1", "Desc1", time, time);
		getCourseDto2 = new GetCourseDto(2L, "Course2", "Desc2", time, time);
		createCourseDto1 = new CreateCourseDto("Course1", "Desc1");
		createCourseDto2 = new CreateCourseDto("Course2", "Desc2");
		updateCourseDto1 = new UpdateCourseDto("UpdatedCourse1", "UpdatedDesc1");
		updateCourseDto2 = new UpdateCourseDto("UpdatedCourse2", "UpdatedDesc2");
	}


	@InjectMocks
	private CourseServiceImpl courseService;

	@Mock
	private CourseRepository courseRepository;
	@Mock
	private CourseMapper courseMapper;


	@Test
	void getAllCourses() {

		Mockito.when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
		Mockito.when(courseMapper.mapToGetCourseDto(course1)).thenReturn(getCourseDto1);
		Mockito.when(courseMapper.mapToGetCourseDto(course2)).thenReturn(getCourseDto2);

		List<GetCourseDto> courses = courseService.getAllCourses();

		Assertions.assertNotEquals(0, courses.size());
		Assertions.assertEquals(2, courses.size());
		Assertions.assertNotNull(courses.get(0));
		Assertions.assertNotNull(courses.get(1));
		Assertions.assertEquals("Course1", courses.get(0).getName());
		Assertions.assertEquals("Course2", courses.get(1).getName());
	}


	@Test
	void getCourseById() {
		Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
		Mockito.when(courseMapper.mapToGetCourseDto(course1)).thenReturn(getCourseDto1);

		GetCourseDto getCourseDto1 = courseService.getCourseById(1L);

		Assertions.assertNotNull(getCourseDto1);
		Assertions.assertEquals("Course1", course1.getName());

		Assertions.assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(3L));
	}


	@Test
	void createCourse() {
		Mockito.when(courseRepository.save(course1)).thenReturn(course1);
		Mockito.when(courseMapper.mapCreateCourseDtoToEntity(createCourseDto1)).thenReturn(course1);
		Mockito.when(courseMapper.mapToGetCourseDto(course1)).thenReturn(getCourseDto1);

		GetCourseDto result = courseService.createCourse(createCourseDto1);

		Mockito.verify(courseRepository, Mockito.times(1)).save(course1);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1L, result.getId());
		Assertions.assertEquals("Course1", result.getName());

	}


//	@Test
//	void updateCourse() {
//		Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
//		Mockito.when(courseMapper.mapToGetCourseDto(course1)).thenReturn(getCourseDto1);
//		Mockito.when(courseRepository.save(course1)).thenReturn(course1);
//
//		GetCourseDto result = courseService.updateCourse(1L, updateCourseDto1);
//
//		Mockito.verify(courseRepository, Mockito.times(1)).save(course1);
//		Assertions.assertNotEquals(1L, result.getId());
//		Assertions.assertNotEquals("Course1", result.getName());
//		Assertions.assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(3L));
//	}

}
