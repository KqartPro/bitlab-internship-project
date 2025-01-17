package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.main.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.main.entities.Course;
import kz.pryahin.bitlabInternship.main.exceptions.BlankNameException;
import kz.pryahin.bitlabInternship.main.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.main.mappers.CourseMapper;
import kz.pryahin.bitlabInternship.main.repositories.CourseRepository;
import kz.pryahin.bitlabInternship.main.services.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CourseMapper courseMapper;

  @InjectMocks
  private CourseServiceImpl courseService;

  private Course course;
  private GetCourseDto getCourseDto;
  private CreateCourseDto createCourseDto;
  private UpdateCourseDto updateCourseDto;


  @BeforeEach
  public void setUp() {
    course = new Course();
    course.setId(1L);
    course.setName("Test Course");
    course.setDescription("Test Description");

    getCourseDto = new GetCourseDto();
    getCourseDto.setId(1L);
    getCourseDto.setName("Test Course");
    getCourseDto.setDescription("Test Description");

    createCourseDto = new CreateCourseDto();
    createCourseDto.setName("New Course");
    createCourseDto.setDescription("New Description");

    updateCourseDto = new UpdateCourseDto();
    updateCourseDto.setName("Updated Course");
    updateCourseDto.setDescription("Updated Description");
  }


  @Test
  public void testGetAllCourses_Success() {
    when(courseRepository.findAll()).thenReturn(List.of(course));
    when(courseMapper.mapToGetCourseDto(any(Course.class))).thenReturn(getCourseDto);

    List<GetCourseDto> result = courseService.getAllCourses();

    assertEquals(1, result.size());
    assertEquals(getCourseDto, result.get(0));

    verify(courseRepository, times(1)).findAll();
    verify(courseMapper, times(1)).mapToGetCourseDto(any(Course.class));
  }


  @Test
  public void testGetCourseById_Success() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
    when(courseMapper.mapToGetCourseDto(any(Course.class))).thenReturn(getCourseDto);

    GetCourseDto result = courseService.getCourseById(1L);

    assertEquals(getCourseDto, result);

    verify(courseRepository, times(1)).findById(1L);
    verify(courseMapper, times(1)).mapToGetCourseDto(any(Course.class));
  }


  @Test
  public void testGetCourseById_NotFound() {
    when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.getCourseById(1L);
    });

    verify(courseRepository, times(1)).findById(1L);
  }


  @Test
  public void testCreateCourse_Success() {
    when(courseMapper.mapCreateCourseDtoToEntity(any(CreateCourseDto.class))).thenReturn(course);
    when(courseRepository.save(any(Course.class))).thenReturn(course);
    when(courseMapper.mapToGetCourseDto(any(Course.class))).thenReturn(getCourseDto);

    GetCourseDto result = courseService.createCourse(createCourseDto);

    assertEquals(getCourseDto, result);

    verify(courseMapper, times(1)).mapCreateCourseDtoToEntity(any(CreateCourseDto.class));
    verify(courseRepository, times(1)).save(any(Course.class));
    verify(courseMapper, times(1)).mapToGetCourseDto(any(Course.class));
  }


  @Test
  public void testUpdateCourse_Success() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
    when(courseRepository.save(any(Course.class))).thenReturn(course);
    when(courseMapper.mapToGetCourseDto(any(Course.class))).thenReturn(getCourseDto);

    GetCourseDto result = courseService.updateCourse(1L, updateCourseDto);

    assertEquals(getCourseDto, result);
    assertEquals("Updated Course", course.getName());
    assertEquals("Updated Description", course.getDescription());

    verify(courseRepository, times(1)).findById(1L);
    verify(courseRepository, times(1)).save(any(Course.class));
    verify(courseMapper, times(1)).mapToGetCourseDto(any(Course.class));
  }


  @Test
  public void testUpdateCourse_NotFound() {
    when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.updateCourse(1L, updateCourseDto);
    });

    verify(courseRepository, times(1)).findById(1L);
  }


  @Test
  public void testUpdateCourse_BlankName() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
    updateCourseDto.setName(" ");

    assertThrows(BlankNameException.class, () -> {
      courseService.updateCourse(1L, updateCourseDto);
    });

    verify(courseRepository, times(1)).findById(1L);
  }


  @Test
  public void testDeleteCourse_Success() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

    courseService.deleteCourse(1L);

    verify(courseRepository, times(1)).findById(1L);
    verify(courseRepository, times(1)).delete(any(Course.class));
  }


  @Test
  public void testDeleteCourse_NotFound() {
    when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.deleteCourse(1L);
    });

    verify(courseRepository, times(1)).findById(1L);
    verify(courseRepository, never()).delete(any(Course.class));
  }
}
