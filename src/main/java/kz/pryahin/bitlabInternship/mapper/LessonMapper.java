package kz.pryahin.bitlabInternship.mapper;

import kz.pryahin.bitlabInternship.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.entities.Lesson;
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
