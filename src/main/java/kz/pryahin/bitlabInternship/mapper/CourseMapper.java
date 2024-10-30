package kz.pryahin.bitlabInternship.mapper;

import kz.pryahin.bitlabInternship.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

	GetCourseDto mapToGetCourseDto(Course course);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdTime", ignore = true)
	@Mapping(target = "updatedTime", ignore = true)
	Course mapCreateCourseDtoToEntity(CreateCourseDto createCourseDto);
}
