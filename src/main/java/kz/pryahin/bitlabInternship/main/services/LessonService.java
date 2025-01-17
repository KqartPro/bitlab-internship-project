package kz.pryahin.bitlabInternship.main.services;

import jakarta.validation.Valid;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.UpdateLessonDto;

import java.util.List;

public interface LessonService {
  List<GetLessonDto> getAllLessons();

  List<GetLessonDto> getAllLessonsFromChapter(Long courseId, Long chapterId);

  GetLessonDto getLessonById(Long courseId, Long chapterId, Long lessonId);

  GetLessonDto createLesson(Long courseId, Long chapterId, @Valid CreateLessonDto createLessonDto);

  GetLessonDto updateLesson(Long courseId, Long chapterId, Long lessonId, @Valid UpdateLessonDto updateLessonDto);

  GetLessonDto updateLessonOrder(Long courseId, Long chapterId, Long lessonId, int lessonOrder);

  void deleteLesson(Long courseId, Long chapterId, Long lessonId);
}
