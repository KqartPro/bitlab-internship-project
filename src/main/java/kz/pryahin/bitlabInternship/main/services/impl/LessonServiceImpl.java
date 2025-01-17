package kz.pryahin.bitlabInternship.main.services.impl;

import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.UpdateLessonDto;
import kz.pryahin.bitlabInternship.main.entities.Chapter;
import kz.pryahin.bitlabInternship.main.entities.Lesson;
import kz.pryahin.bitlabInternship.main.exceptions.BlankNameException;
import kz.pryahin.bitlabInternship.main.exceptions.ChapterNotFoundException;
import kz.pryahin.bitlabInternship.main.exceptions.LessonNotFoundException;
import kz.pryahin.bitlabInternship.main.mappers.LessonMapper;
import kz.pryahin.bitlabInternship.main.repositories.ChapterRepository;
import kz.pryahin.bitlabInternship.main.repositories.LessonRepository;
import kz.pryahin.bitlabInternship.main.services.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonServiceImpl implements LessonService {
  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;
  private final ChapterRepository chapterRepository;


  private void isCourseContainsChapter(Long courseId, Long chapterId) {
    log.debug("Запускаем метод isCourseContainsChapter с параметрами courseId: {}, chapterId: {}", courseId, chapterId);
    Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> {
      log.error("Глава с ID {} не найдена", chapterId);
      return new ChapterNotFoundException();
    });

    if (!chapter.getCourse().getId().equals(courseId)) {
      log.error("Курс с ID {} не содержит главу с ID {}", courseId, chapterId);
      throw new IllegalArgumentException("This course doesn't contain the requested chapter");
    }
    log.debug("Курс с ID {} содержит главу с ID {}", courseId, chapterId);
    log.info("Метод isCourseContainsChapter выполнен");
  }


  @Override
  public List<GetLessonDto> getAllLessons() {
    log.info("Вызван метод getAllLessons");

    List<Lesson> lessons = lessonRepository.findAll();
    log.debug("Найдено {} уроков", lessons.size());

    log.info("Метод getAllLessons выполнен");
    return lessons.stream()
        .map(lessonMapper::mapToGetLessonDto)
        .toList();
  }


  @Override
  public List<GetLessonDto> getAllLessonsFromChapter(Long courseId, Long chapterId) {
    log.info("Вызван метод getAllLessonsFromChapter для курса с ID {} и главы с ID {}", courseId, chapterId);
    isCourseContainsChapter(courseId, chapterId);

    List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);
    lessons.sort(Comparator.comparing(Lesson::getLessonOrder));
    log.debug("Найдено {} уроков в главе с ID {}", lessons.size(), chapterId);


    log.info("Метод getAllLessonsFromChapter выполнен");
    return lessons.stream()
        .map(lessonMapper::mapToGetLessonDto)
        .toList();
  }


  @Override
  public GetLessonDto getLessonById(Long courseId, Long chapterId, Long lessonId) {
    log.info("Вызван метод getLessonById для курса с ID {}, главы с ID {} и урока с ID {}", courseId, chapterId, lessonId);
    isCourseContainsChapter(courseId, chapterId);

    Lesson lesson = lessonRepository.findByChapterIdAndId(chapterId, lessonId)
        .orElseThrow(() -> {
          log.error("Урок с ID {} не найден в главе с ID {}", lessonId, chapterId);
          return new LessonNotFoundException();
        });
    log.debug("Урок найден: {}", lesson);
    log.info("Метод getLessonById выполнен");
    return lessonMapper.mapToGetLessonDto(lesson);
  }


  @Override
  public GetLessonDto createLesson(Long courseId, Long chapterId, CreateLessonDto createLessonDto) {
    log.info("Вызван метод createLesson для курса с ID {} и главы с ID {}", courseId, chapterId);
    isCourseContainsChapter(courseId, chapterId);

    log.debug("Маппинг CreateLessonDto в сущность Lesson");
    Lesson lesson = lessonMapper.mapCreateLessonDtoToEntity(createLessonDto);

    Chapter chapter = chapterRepository.findById(chapterId)
        .orElseThrow(() -> {
          log.error("Глава с ID {} не найдена", chapterId);
          return new ChapterNotFoundException();
        });

    lesson.setChapter(chapter);

    List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);
    Optional<Lesson> lessonWithMaxOrderValue = lessons.stream()
        .max(Comparator.comparing(Lesson::getLessonOrder));

    if (lessonWithMaxOrderValue.isPresent()) {
      lesson.setLessonOrder(lessonWithMaxOrderValue.get().getLessonOrder() + 1);
      log.debug("Установлен порядок урока: {}", lesson.getLessonOrder());
    } else {
      lesson.setLessonOrder(1);
      log.debug("Установлен порядок урока по умолчанию: 1");
    }

    Lesson savedLesson = lessonRepository.save(lesson);
    log.debug("Урок успешно создан с ID: {}", savedLesson.getId());
    log.info("Метод createLesson выполнен");
    return lessonMapper.mapToGetLessonDto(savedLesson);
  }


  @Override
  public GetLessonDto updateLesson(Long courseId, Long chapterId, Long lessonId, UpdateLessonDto updateLessonDto) {
    log.info("Вызван метод updateLesson для курса с ID {}, главы с ID {} и урока с ID {}", courseId, chapterId, lessonId);
    isCourseContainsChapter(courseId, chapterId);

    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> {
      log.error("Урок с ID {} не найден", lessonId);
      return new LessonNotFoundException();
    });

    if (updateLessonDto.getName() != null) {
      if (!updateLessonDto.getName().isBlank()) {
        lesson.setName(updateLessonDto.getName());
        log.debug("Имя урока обновлено: {}", updateLessonDto.getName());
      } else {
        log.error("Имя урока пустое");
        throw new BlankNameException();
      }
    }

    if (updateLessonDto.getDescription() != null) {
      lesson.setDescription(updateLessonDto.getDescription());
      log.debug("Описание урока обновлено");
    }

    if (updateLessonDto.getContent() != null) {
      lesson.setContent(updateLessonDto.getContent());
      log.debug("Контент урока обновлен");
    }

    if (updateLessonDto.getChapterId() != null) {
      log.debug("Обновление главы урока на главу с ID {}", updateLessonDto.getChapterId());
      Chapter chapter = chapterRepository.findById(updateLessonDto.getChapterId())
          .orElseThrow(() -> {
            log.error("Глава с ID {} не найдена", updateLessonDto.getChapterId());
            return new ChapterNotFoundException(
                "The chapter to which you are adding the Lesson is not found");
          });

      List<Lesson> lessons = lessonRepository.findAllByChapterId(updateLessonDto.getChapterId());
      Optional<Lesson> lessonWithMaxOrder = lessons.stream().max(Comparator.comparing(Lesson::getLessonOrder));

      if (lessonWithMaxOrder.isPresent()) {
        lesson.setLessonOrder(lessonWithMaxOrder.get().getLessonOrder() + 1);
        log.debug("Установлен новый порядок урока: {}", lesson.getLessonOrder());
      } else {
        lesson.setLessonOrder(1);
        log.debug("Установлен порядок урока по умолчанию: 1");
      }

      lesson.setChapter(chapter);
      log.debug("Глава урока обновлена на главу с ID {}", updateLessonDto.getChapterId());
    }

    Lesson updatedLesson = lessonRepository.save(lesson);
    log.debug("Урок с ID {} успешно обновлен", updatedLesson.getId());

    log.info("Метод updateLesson выполнен");
    return lessonMapper.mapToGetLessonDto(updatedLesson);
  }


  @Override
  public GetLessonDto updateLessonOrder(Long courseId, Long chapterId, Long lessonId, int lessonOrder) {
    log.info("Вызван метод updateLessonOrder для курса с ID {}, главы с ID {}, урока с ID {}, новый порядок: {}",
        courseId, chapterId, lessonId, lessonOrder);
    isCourseContainsChapter(courseId, chapterId);

    List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);
    Optional<Lesson> lessonWithMaxOrder = lessons.stream().max(Comparator.comparing(Lesson::getLessonOrder));

    if (lessonWithMaxOrder.isPresent()) {
      if (lessonOrder > lessonWithMaxOrder.get().getLessonOrder()) {
        log.error("Новый порядок {} больше максимального порядка {}", lessonOrder, lessonWithMaxOrder.get().getLessonOrder());
        throw new IllegalArgumentException("New order number must be less than the greatest");
      }
      if (lessonOrder <= 0) {
        log.error("Новый порядок {} меньше или равен 0", lessonOrder);
        throw new IllegalArgumentException("New order number must be greater than 0");
      }
    }

    lessons.sort(Comparator.comparing(Lesson::getLessonOrder));

    Lesson lessonToChange = lessons.stream()
        .filter(lesson -> lesson.getId().equals(lessonId))
        .findFirst()
        .orElseThrow(() -> {
          log.error("Урок с ID {} не найден в главе с ID {}", lessonId, chapterId);
          return new LessonNotFoundException();
        });

    int oldOrder = lessonToChange.getLessonOrder();
    log.debug("Текущий порядок урока: {}, новый порядок: {}", oldOrder, lessonOrder);

    if (lessonOrder > oldOrder) {
      log.debug("Новый порядок больше старого, сдвигаем остальные уроки назад");
      lessons.stream()
          .filter(lesson -> lesson.getLessonOrder() > oldOrder && lesson.getLessonOrder() <= lessonOrder)
          .forEach(lesson -> lesson.setLessonOrder(lesson.getLessonOrder() - 1));
    } else if (lessonOrder < oldOrder) {
      log.debug("Новый порядок меньше старого, сдвигаем остальные уроки вперед");
      lessons.stream()
          .filter(lesson -> lesson.getLessonOrder() >= lessonOrder && lesson.getLessonOrder() < oldOrder)
          .forEach(lesson -> lesson.setLessonOrder(lesson.getLessonOrder() + 1));
    }

    lessonToChange.setLessonOrder(lessonOrder);
    log.debug("Обновлен порядок урока с ID {} на {}", lessonId, lessonOrder);

    lessonRepository.saveAll(lessons);
    log.info("Метод updateLessonOrder выполнен успешно");
    return lessonMapper.mapToGetLessonDto(lessonToChange);
  }


  @Override
  public void deleteLesson(Long courseId, Long chapterId, Long lessonId) {
    log.info("Вызван метод deleteLesson для курса с ID {}, главы с ID {}, урока с ID {}", courseId, chapterId, lessonId);
    isCourseContainsChapter(courseId, chapterId);

    Lesson lesson = lessonRepository.findByChapterIdAndId(chapterId, lessonId)
        .orElseThrow(() -> {
          log.error("Урок с ID {} не найден в главе с ID {}", lessonId, chapterId);
          return new LessonNotFoundException();
        });
    lessonRepository.delete(lesson);
    log.info("Урок с ID {} успешно удален", lessonId);
  }
}



