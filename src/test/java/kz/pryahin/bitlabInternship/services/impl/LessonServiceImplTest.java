package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.main.dtos.lessonDtos.UpdateLessonDto;
import kz.pryahin.bitlabInternship.main.entities.Chapter;
import kz.pryahin.bitlabInternship.main.entities.Course;
import kz.pryahin.bitlabInternship.main.entities.Lesson;
import kz.pryahin.bitlabInternship.main.exceptions.ChapterNotFoundException;
import kz.pryahin.bitlabInternship.main.exceptions.LessonNotFoundException;
import kz.pryahin.bitlabInternship.main.mappers.LessonMapper;
import kz.pryahin.bitlabInternship.main.repositories.ChapterRepository;
import kz.pryahin.bitlabInternship.main.repositories.LessonRepository;
import kz.pryahin.bitlabInternship.main.services.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonMapper lessonMapper;

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Chapter chapter;
    private Lesson lesson;
    private GetLessonDto getLessonDto;
    private CreateLessonDto createLessonDto;
    private UpdateLessonDto updateLessonDto;


    @BeforeEach
    public void setUp() {
        Course course = new Course();
        course.setId(1L);

        chapter = new Chapter();
        chapter.setId(1L);
        chapter.setCourse(course);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setChapter(chapter);

        getLessonDto = new GetLessonDto();
        getLessonDto.setId(1L);

        createLessonDto = new CreateLessonDto();
        createLessonDto.setName("New Lesson");

        updateLessonDto = new UpdateLessonDto();
        updateLessonDto.setName("Updated Lesson");
    }


    @Test
    public void testGetAllLessons_Success() {
        when(lessonRepository.findAll()).thenReturn(List.of(lesson));
        when(lessonMapper.mapToGetLessonDto(any(Lesson.class))).thenReturn(getLessonDto);

        List<GetLessonDto> result = lessonService.getAllLessons();

        assertEquals(1, result.size());
        assertEquals(getLessonDto, result.get(0));

        verify(lessonRepository, times(1)).findAll();
        verify(lessonMapper, times(1)).mapToGetLessonDto(any(Lesson.class));
    }


    @Test
    public void testGetLessonById_Success() {
        when(lessonRepository.findByChapterIdAndId(1L, 1L)).thenReturn(Optional.of(lesson));

        when(lessonMapper.mapToGetLessonDto(any(Lesson.class))).thenReturn(getLessonDto);

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        GetLessonDto result = lessonService.getLessonById(1L, 1L, 1L);

        assertEquals(getLessonDto, result);

        verify(lessonRepository, times(1)).findByChapterIdAndId(1L, 1L);
        verify(lessonMapper, times(1)).mapToGetLessonDto(any(Lesson.class));
    }


    @Test
    public void testGetLessonById_NotFound() {
        when(lessonRepository.findByChapterIdAndId(1L, 1L)).thenReturn(Optional.empty());

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        assertThrows(LessonNotFoundException.class, () -> lessonService.getLessonById(1L, 1L, 1L));

        verify(lessonRepository, times(1)).findByChapterIdAndId(1L, 1L);
    }


    @Test
    public void testCreateLesson_Success() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(lessonMapper.mapCreateLessonDtoToEntity(any(CreateLessonDto.class))).thenReturn(lesson);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(lessonMapper.mapToGetLessonDto(any(Lesson.class))).thenReturn(getLessonDto);

        GetLessonDto result = lessonService.createLesson(1L, 1L, createLessonDto);

        assertEquals(getLessonDto, result);

        verify(chapterRepository, times(2)).findById(1L);
        verify(lessonMapper, times(1)).mapCreateLessonDtoToEntity(any(CreateLessonDto.class));
        verify(lessonRepository, times(1)).save(any(Lesson.class));
        verify(lessonMapper, times(1)).mapToGetLessonDto(any(Lesson.class));
    }


    @Test
    public void testCreateLesson_ChapterNotFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ChapterNotFoundException.class, () -> lessonService.createLesson(1L, 1L, createLessonDto));

        verify(chapterRepository, times(1)).findById(1L);
        verify(lessonRepository, never()).save(any(Lesson.class));
    }


    @Test
    public void testUpdateLesson_Success() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(lessonMapper.mapToGetLessonDto(any(Lesson.class))).thenReturn(getLessonDto);

        GetLessonDto result = lessonService.updateLesson(1L, 1L, 1L, updateLessonDto);

        assertEquals(getLessonDto, result);
        assertEquals("Updated Lesson", lesson.getName());

        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
        verify(lessonMapper, times(1)).mapToGetLessonDto(any(Lesson.class));
    }


    @Test
    public void testUpdateLesson_NotFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.updateLesson(1L, 1L, 1L, updateLessonDto));

        verify(lessonRepository, times(1)).findById(1L);
    }


    @Test
    public void testUpdateLessonOrder_Success() {
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setLessonOrder(1);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setLessonOrder(2);
        Lesson lesson3 = new Lesson();
        lesson3.setId(3L);
        lesson3.setLessonOrder(3);
        Lesson lesson4 = new Lesson();
        lesson4.setId(4L);
        lesson4.setLessonOrder(4);

        List<Lesson> lessons = new ArrayList<>(List.of(lesson1, lesson2, lesson3, lesson4));

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findAllByChapterId(1L)).thenReturn(lessons);
        when(lessonRepository.saveAll(anyList())).thenReturn(lessons);
        when(lessonMapper.mapToGetLessonDto(any(Lesson.class))).thenReturn(new GetLessonDto());

        GetLessonDto result = lessonService.updateLessonOrder(1L, 1L, 2L, 4);

        assertNotNull(result);
        assertEquals(1, lesson1.getLessonOrder());
        assertEquals(4, lesson2.getLessonOrder());
        assertEquals(2, lesson3.getLessonOrder());
        assertEquals(3, lesson4.getLessonOrder());

        verify(lessonRepository, times(1)).saveAll(anyList());
    }


    @Test
    public void testUpdateLessonOrder_OrderGreaterThanMax() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setLessonOrder(1);

        when(lessonRepository.findAllByChapterId(1L)).thenReturn(List.of(lesson));

        assertThrows(IllegalArgumentException.class, () -> lessonService.updateLessonOrder(1L, 1L, 1L, 3));

        verify(lessonRepository, never()).saveAll(anyList());
    }


    @Test
    public void testDeleteLesson_Success() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        when(lessonRepository.findByChapterIdAndId(1L, 1L)).thenReturn(Optional.of(lesson));

        lessonService.deleteLesson(1L, 1L, 1L);

        verify(lessonRepository, times(1)).findByChapterIdAndId(1L, 1L);
        verify(lessonRepository, times(1)).delete(any(Lesson.class));
    }


    @Test
    public void testDeleteLesson_NotFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        when(lessonRepository.findByChapterIdAndId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.deleteLesson(1L, 1L, 1L));

        verify(lessonRepository, times(1)).findByChapterIdAndId(1L, 1L);
        verify(lessonRepository, never()).delete(any(Lesson.class));
    }
}

