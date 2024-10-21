package kz.pryahin.bitlabInternship.mapper;

import kz.pryahin.bitlabInternship.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.entities.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
	GetLessonDto mapToGetLessonDto(Lesson lesson);

	Lesson mapCreateLessonDtoToEntity(CreateLessonDto createLessonDto);
}
