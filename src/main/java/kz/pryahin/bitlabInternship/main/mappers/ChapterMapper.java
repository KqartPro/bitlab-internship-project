package kz.pryahin.bitlabInternship.main.mappers;

import kz.pryahin.bitlabInternship.main.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.main.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.main.entities.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
  GetChapterDto mapToGetChapterDto(Chapter chapter);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdTime", ignore = true)
  @Mapping(target = "updatedTime", ignore = true)
  @Mapping(target = "chapterOrder", ignore = true)
  @Mapping(target = "course", ignore = true)
  Chapter mapCreateChapterDtoToEntity(CreateChapterDto createChapterDto);
}
