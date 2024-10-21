package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.entities.Course;
import kz.pryahin.bitlabInternship.exceptions.BlankNameException;
import kz.pryahin.bitlabInternship.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.mapper.CourseMapper;
import kz.pryahin.bitlabInternship.repositories.CourseRepository;
import kz.pryahin.bitlabInternship.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;


	@Override
	public List<GetCourseDto> getAllCourses() {
		return courseRepository.findAll().stream()
			.map(courseMapper::mapToGetCourseDto)
			.toList(); // .collect(Collectors.toList()); - собрать в изменяемый список, .toList(); - неизменяемый
	}


	@Override
	public GetCourseDto getCourseById(Long id) {
		return courseMapper.mapToGetCourseDto(courseRepository.findById(id).orElseThrow(CourseNotFoundException::new));
	}


	@Override
	public GetCourseDto createCourse(CreateCourseDto createCourseDto) {
		Course course = courseMapper.mapCreateCourseDtoToEntity(createCourseDto);
		return courseMapper.mapToGetCourseDto(courseRepository.save(course));
	}


	@Override
	public GetCourseDto updateCourse(Long id, UpdateCourseDto updateCourseDto) {
		Course course = courseRepository.findById(id)
			.orElseThrow(CourseNotFoundException::new);

		if (updateCourseDto.getName() != null) {
			if (!updateCourseDto.getName().isBlank()) {
				course.setName(updateCourseDto.getName());
			} else {
				throw new BlankNameException();
			}
		}

		if (updateCourseDto.getDescription() != null) {
			course.setDescription(updateCourseDto.getDescription());
		}

		return courseMapper.mapToGetCourseDto(courseRepository.save(course));
	}

	@Override
	public void deleteCourse(Long id) {
		Course course = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
		courseRepository.delete(course);
	}
}
