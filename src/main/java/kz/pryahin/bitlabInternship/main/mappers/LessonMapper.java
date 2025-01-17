package kz.pryahin.bitlabInternship.main.mappers;

import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.main.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {
  GetLessonDto mapToGetLessonDto(Lesson lesson);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdTime", ignore = true)
  @Mapping(target = "updatedTime", ignore = true)
  @Mapping(target = "lessonOrder", ignore = true)
  @Mapping(target = "chapter", ignore = true)
  Lesson mapCreateLessonDtoToEntity(CreateLessonDto createLessonDto);
}
