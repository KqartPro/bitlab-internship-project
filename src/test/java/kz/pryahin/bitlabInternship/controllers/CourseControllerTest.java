package kz.pryahin.bitlabInternship.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.pryahin.bitlabInternship.main.controllers.CourseController;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.CreateCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.GetCourseDto;
import kz.pryahin.bitlabInternship.main.dtos.courseDtos.UpdateCourseDto;
import kz.pryahin.bitlabInternship.main.entities.Course;
import kz.pryahin.bitlabInternship.main.services.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private GetCourseDto getCourseDto;

  @InjectMocks
  private CourseController courseController;

  @Mock
  private CourseServiceImpl courseService;


  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    objectMapper = new ObjectMapper();

    Course course = new Course();
    course.setId(1L);
    course.setName("Test Course");
    course.setDescription("Test Description");

    getCourseDto = new GetCourseDto();
    getCourseDto.setId(1L);
    getCourseDto.setName("Test Course Dto");
    getCourseDto.setDescription("Test Description");
  }


  @Test
  void getAllCourses() throws Exception {
    when(courseService.getAllCourses()).thenReturn(List.of(getCourseDto));

    mockMvc.perform(get("/course/get-all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Test Course Dto"));
    verify(courseService, times(1)).getAllCourses();
  }


  @Test
  void getCourseById() throws Exception {
    when(courseService.getCourseById(1L)).thenReturn(getCourseDto);

    mockMvc.perform(get("/course/get-one/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Test Course Dto"))
        .andExpect(jsonPath("$.description").value("Test Description"));


    verify(courseService, times(1)).getCourseById(1L);
  }


  @Test
  void createCourse() throws Exception {
    CreateCourseDto course = new CreateCourseDto();
    course.setName("First Course");

    mockMvc.perform(post("/course/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(course)))
        .andExpect(status().isCreated());

    verify(courseService, times(1)).createCourse(course);

  }


  @Test
  void updateCourse() throws Exception {
    UpdateCourseDto course = new UpdateCourseDto();
    course.setName("Test Course Dto");
    course.setDescription("Test Description");

    String courseJson = objectMapper.writeValueAsString(course);
    mockMvc.perform(patch("/course/update/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(courseJson))
        .andExpect(status().isOk());

    verify(courseService, times(1)).updateCourse(1L, course);

  }


  @Test
  public void updateCourse_ShouldReturnUpdatedCourse() throws Exception {
    when(courseService.updateCourse(eq(1L), any(UpdateCourseDto.class))).thenReturn(getCourseDto);

    UpdateCourseDto course = new UpdateCourseDto();
    course.setName("Test Course Dto");
    course.setDescription("Test Description");

    mockMvc.perform(patch("/course/update/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(course)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(getCourseDto.getName()))
        .andExpect(jsonPath("$.description").value(getCourseDto.getDescription()));

    verify(courseService, times(1)).updateCourse(eq(1L), any(UpdateCourseDto.class));
  }


  @Test
  public void deleteCourse_ShouldReturnStatusOk() throws Exception {
    mockMvc.perform(delete("/course/delete/{id}", 1L))
        .andExpect(status().isOk());

    verify(courseService, times(1)).deleteCourse(1L);
  }
}
