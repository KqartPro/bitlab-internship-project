package kz.pryahin.bitlabInternship.main.services.impl;

import kz.pryahin.bitlabInternship.main.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.main.entities.Course;
import kz.pryahin.bitlabInternship.main.exceptions.BlankNameException;
import kz.pryahin.bitlabInternship.main.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.main.mappers.CourseMapper;
import kz.pryahin.bitlabInternship.main.repositories.CourseRepository;
import kz.pryahin.bitlabInternship.main.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;


  @Override
  public List<GetCourseDto> getAllCourses() {
    log.info("Вызван метод getAllCourses");

    List<Course> courses = courseRepository.findAll();
    log.debug("Найдено {} курсов", courses.size());

    log.info("Метод getAllCourses выполнен");
    return courses.stream()
        .map(courseMapper::mapToGetCourseDto)
        .toList();
  }


  @Override
  public GetCourseDto getCourseById(Long id) {
    log.info("Вызван метод getCourseById с ID: {}", id);
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Курс с ID {} не найден", id);
          return new CourseNotFoundException();
        });
    log.debug("Курс найден: {}", course);

    log.info("Метод getCourseById выполнен");
    return courseMapper.mapToGetCourseDto(course);
  }


  @Override
  public GetCourseDto createCourse(CreateCourseDto createCourseDto) {
    log.info("Вызван метод createCourse с данными: {}", createCourseDto);

    Course course = courseMapper.mapCreateCourseDtoToEntity(createCourseDto);
    log.debug("Маппинг CreateCourseDto в Course: {}", course);
    course = courseRepository.save(course);
    log.debug("Курс сохранен: {}", course);

    log.info("Метод createCourse выполнен");
    return courseMapper.mapToGetCourseDto(course);
  }


  @Override
  public GetCourseDto updateCourse(Long id, UpdateCourseDto updateCourseDto) {
    log.info("Вызван метод updateCourse");

    log.debug("В метод updateCourse с ID: {} переданы данные: {}", id, updateCourseDto);
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Курс с ID {} не найден для обновления", id);
          return new CourseNotFoundException();
        });

    if (updateCourseDto.getName() != null) {
      if (!updateCourseDto.getName().isBlank()) {
        course.setName(updateCourseDto.getName());
        log.debug("Название курса обновлено на: {}", updateCourseDto.getName());
      } else {
        log.error("Попытка установить пустое название курса");
        throw new BlankNameException();
      }
    }

    if (updateCourseDto.getDescription() != null) {
      course.setDescription(updateCourseDto.getDescription());
      log.debug("Описание курса обновлено на: {}", updateCourseDto.getDescription());
    }

    course = courseRepository.save(course);
    log.debug("Курс обновлен: {}", course);

    log.info("Метод updateCourse выполнен");
    return courseMapper.mapToGetCourseDto(course);
  }


  @Override
  public void deleteCourse(Long id) {
    log.info("Вызван метод deleteCourse с ID: {}", id);

    Course course = courseRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Курс с ID {} не найден для удаления", id);
          return new CourseNotFoundException();
        });
    courseRepository.delete(course);
    log.debug("Курс с ID {} успешно удален", id);

    log.info("Метод deleteCourse выполнен");
  }
}
