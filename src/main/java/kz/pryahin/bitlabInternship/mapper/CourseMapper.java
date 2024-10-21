package kz.pryahin.bitlabInternship.mapper;

import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.entities.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

	GetCourseDto mapToGetCourseDto(Course course);

	Course mapCreateCourseDtoToEntity(CreateCourseDto createCourseDto);
}
