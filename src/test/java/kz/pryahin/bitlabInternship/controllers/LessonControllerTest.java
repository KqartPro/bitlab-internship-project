package kz.pryahin.bitlabInternship.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.pryahin.bitlabInternship.main.controllers.LessonController;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.UpdateLessonDto;
import kz.pryahin.bitlabInternship.main.services.LessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LessonController.class)
@WithMockUser(username = "admin", roles = {"ADMIN", "USER", "TEACHER"})
class LessonControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;


    @Test
    void getAllLessons() throws Exception {
        GetLessonDto lesson1 = new GetLessonDto();
        lesson1.setId(1L);
        lesson1.setName("Lesson 1");
        lesson1.setDescription("Description 1");
        lesson1.setLessonOrder(1);

        GetLessonDto lesson2 = new GetLessonDto();
        lesson2.setId(2L);
        lesson2.setName("Lesson 2");
        lesson2.setDescription("Description 2");
        lesson2.setLessonOrder(2);

        when(lessonService.getAllLessons()).thenReturn(List.of(lesson1, lesson2));

        mockMvc.perform(get("/lesson/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lesson 1"))
                .andExpect(jsonPath("$[1].name").value("Lesson 2"));

        verify(lessonService, times(1)).getAllLessons();
    }


    @Test
    void getAllLessonsFromChapter() throws Exception {
        Long courseId = 1L;
        Long chapterId = 1L;
        GetLessonDto lesson1 = new GetLessonDto();
        lesson1.setId(1L);
        lesson1.setName("Lesson 1");
        lesson1.setDescription("Description 1");
        lesson1.setLessonOrder(1);

        GetLessonDto lesson2 = new GetLessonDto();
        lesson2.setId(2L);
        lesson2.setName("Lesson 2");
        lesson2.setDescription("Description 2");
        lesson2.setLessonOrder(2);

        when(lessonService.getAllLessonsFromChapter(courseId, chapterId)).thenReturn(List.of(lesson1, lesson2));

        mockMvc.perform(get("/lesson/get-all-from-chapter")
                        .param("courseId", courseId.toString())
                        .param("chapterId", chapterId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lesson 1"))
                .andExpect(jsonPath("$[1].name").value("Lesson 2"));

        verify(lessonService, times(1)).getAllLessonsFromChapter(courseId, chapterId);
    }


    @Test
    void getLessonById() throws Exception {
        Long courseId = 1L;
        Long chapterId = 1L;
        Long lessonId = 1L;
        GetLessonDto lesson = new GetLessonDto();
        lesson.setId(lessonId);
        lesson.setName("Lesson 1");
        lesson.setDescription("Description 1");
        lesson.setLessonOrder(1);

        when(lessonService.getLessonById(courseId, chapterId, lessonId)).thenReturn(lesson);

        mockMvc.perform(get("/lesson/get-one-lesson")
                        .param("courseId", courseId.toString())
                        .param("chapterId", chapterId.toString())
                        .param("lessonId", lessonId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lesson 1"));

        verify(lessonService, times(1)).getLessonById(courseId, chapterId, lessonId);
    }


    @Test
    void createLesson() throws Exception {
        Long courseId = 1L;
        Long chapterId = 1L;
        CreateLessonDto createDto = new CreateLessonDto();
        createDto.setName("Lesson 1");
        createDto.setDescription("Description 1");
        createDto.setContent("Content 1");

        GetLessonDto responseDto = new GetLessonDto();
        responseDto.setId(1L);
        responseDto.setName("Lesson 1");
        responseDto.setDescription("Description 1");
        responseDto.setContent("Content 1");
        responseDto.setLessonOrder(1);

        when(lessonService.createLesson(eq(courseId), eq(chapterId), any(CreateLessonDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/lesson")
                        .param("courseId", courseId.toString())
                        .param("chapterId", chapterId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lesson 1"));

        verify(lessonService, times(1)).createLesson(eq(courseId), eq(chapterId), any(CreateLessonDto.class));
    }


    @Test
    void updateLesson() throws Exception {
        Long courseId = 1L;
        Long chapterId = 1L;
        Long lessonId = 1L;
        UpdateLessonDto updateDto = new UpdateLessonDto();
        updateDto.setName("Updated Lesson");
        updateDto.setDescription("Updated Description");
        updateDto.setContent("Updated Content");

        GetLessonDto responseDto = new GetLessonDto();
        responseDto.setId(lessonId);
        responseDto.setName("Updated Lesson");
        responseDto.setDescription("Updated Description");
        responseDto.setContent("Updated Content");
        responseDto.setLessonOrder(2);

        when(lessonService.updateLesson(eq(courseId), eq(chapterId), eq(lessonId), any(UpdateLessonDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/lesson")
                        .param("courseId", courseId.toString())
                        .param("chapterId", chapterId.toString())
                        .param("lessonId", lessonId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Lesson"));

        verify(lessonService, times(1)).updateLesson(eq(courseId), eq(chapterId), eq(lessonId), any(UpdateLessonDto.class));
    }


    @Test
    void updateLessonOrder() throws Exception {
        Long courseId = 1L;
        Long chapterId = 1L;
        Long lessonId = 1L;
        int newOrder = 2;
        GetLessonDto responseDto = new GetLessonDto();
        responseDto.setId(lessonId);
        responseDto.setName("Lesson 1");
        responseDto.setDescription("Description 1");
        responseDto.setContent("Content 1");
        responseDto.setLessonOrder(newOrder);

        when(lessonService.updateLessonOrder(courseId, chapterId, lessonId, newOrder)).thenReturn(responseDto);

        mockMvc.perform(put("/lesson/update-order")
                        .param("courseId", courseId.toString())
                        .param("chapterId", chapterId.toString())
                        .param("lessonId", lessonId.toString())
                        .param("lessonOrder", Integer.toString(newOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonOrder").value(newOrder));

        verify(lessonService, times(1)).updateLessonOrder(courseId, chapterId, lessonId, newOrder);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteLesson() throws Exception {
        Long courseId = 1L;
        Long chapterId = 1L;
        Long lessonId = 1L;

        doNothing().when(lessonService).deleteLesson(courseId, chapterId, lessonId);

        mockMvc.perform(delete("/lesson")
                        .param("courseId", courseId.toString())
                        .param("chapterId", chapterId.toString())
                        .param("lessonId", lessonId.toString()))
                .andExpect(status().isOk());

        verify(lessonService, times(1)).deleteLesson(courseId, chapterId, lessonId);
    }
}
