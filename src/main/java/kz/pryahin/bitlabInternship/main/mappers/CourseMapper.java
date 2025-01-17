package kz.pryahin.bitlabInternship.main.mappers;

import kz.pryahin.bitlabInternship.main.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.main.entities.Course;
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
