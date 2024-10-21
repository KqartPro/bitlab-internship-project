package kz.pryahin.bitlabInternship.mapper;

import kz.pryahin.bitlabInternship.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.entities.Chapter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
	GetChapterDto mapToGetChapterDto(Chapter chapter);

	Chapter mapCreateChapterDtoToEntity(CreateChapterDto createChapterDto);
}
