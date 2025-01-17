package kz.pryahin.bitlabInternship.main.services;

import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.main.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.main.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.main.dtos.chapterDtos.UpdateChapterDto;

import java.util.List;

public interface ChapterService {
  List<GetChapterDto> getAllChapters();

  List<GetChapterDto> getAllChaptersByCourseId(Long courseId);

  GetChapterDto getChapterById(Long courseId, Long chapterId);

  GetChapterDto createChapter(Long courseId, @Valid CreateChapterDto createChapterDto);

  GetChapterDto updateChapter(Long courseId, Long chapterId, @Valid UpdateChapterDto updateChapterDto);

  GetChapterDto updateChapterOrder(Long courseId, Long chapterId, int chapterOrder);

  void deleteChapter(Long courseId, Long chapterId);
}
