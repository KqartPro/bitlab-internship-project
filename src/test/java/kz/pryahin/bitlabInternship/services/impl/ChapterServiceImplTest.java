package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.UpdateChapterDto;
import kz.pryahin.bitlabInternship.entities.Chapter;
import kz.pryahin.bitlabInternship.entities.Course;
import kz.pryahin.bitlabInternship.exceptions.ChapterNotFoundException;
import kz.pryahin.bitlabInternship.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.mapper.ChapterMapper;
import kz.pryahin.bitlabInternship.repositories.ChapterRepository;
import kz.pryahin.bitlabInternship.repositories.CourseRepository;
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
public class ChapterServiceImplTest {

	@Mock
	private ChapterRepository chapterRepository;

	@Mock
	private CourseRepository courseRepository;

	@Mock
	private ChapterMapper chapterMapper;

	@InjectMocks
	private ChapterServiceImpl chapterService;

	private Chapter chapter;
	private Course course;
	private GetChapterDto getChapterDto;
	private CreateChapterDto createChapterDto;
	private UpdateChapterDto updateChapterDto;


	@BeforeEach
	public void setUp() {
		course = new Course();
		course.setId(1L);
		course.setName("Test Course");

		chapter = new Chapter();
		chapter.setId(1L);
		chapter.setName("Test Chapter");
		chapter.setCourse(course);

		getChapterDto = new GetChapterDto();
		getChapterDto.setId(1L);
		getChapterDto.setName("Test Chapter");

		createChapterDto = new CreateChapterDto();
		createChapterDto.setName("New Chapter");

		updateChapterDto = new UpdateChapterDto();
		updateChapterDto.setName("Updated Chapter");
	}


	@Test
	public void testGetAllChapters_Success() {
		when(chapterRepository.findAll()).thenReturn(List.of(chapter));
		when(chapterMapper.mapToGetChapterDto(any(Chapter.class))).thenReturn(getChapterDto);

		List<GetChapterDto> result = chapterService.getAllChapters();

		assertEquals(1, result.size());
		assertEquals(getChapterDto, result.get(0));

		verify(chapterRepository, times(1)).findAll();
		verify(chapterMapper, times(1)).mapToGetChapterDto(any(Chapter.class));
	}


	@Test
	public void testGetChapterById_Success() {
		when(chapterRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.of(chapter));
		when(chapterMapper.mapToGetChapterDto(any(Chapter.class))).thenReturn(getChapterDto);

		GetChapterDto result = chapterService.getChapterById(1L, 1L);

		assertEquals(getChapterDto, result);

		verify(chapterRepository, times(1)).findByCourseIdAndId(1L, 1L);
		verify(chapterMapper, times(1)).mapToGetChapterDto(any(Chapter.class));
	}


	@Test
	public void testGetChapterById_NotFound() {
		when(chapterRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ChapterNotFoundException.class, () -> chapterService.getChapterById(1L, 1L));

		verify(chapterRepository, times(1)).findByCourseIdAndId(1L, 1L);
	}


	@Test
	public void testCreateChapter_Success() {
		when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
		when(chapterMapper.mapCreateChapterDtoToEntity(any(CreateChapterDto.class))).thenReturn(chapter);
		when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);
		when(chapterMapper.mapToGetChapterDto(any(Chapter.class))).thenReturn(getChapterDto);

		GetChapterDto result = chapterService.createChapter(1L, createChapterDto);

		assertEquals(getChapterDto, result);

		verify(courseRepository, times(1)).findById(1L);
		verify(chapterMapper, times(1)).mapCreateChapterDtoToEntity(any(CreateChapterDto.class));
		verify(chapterRepository, times(1)).save(any(Chapter.class));
		verify(chapterMapper, times(1)).mapToGetChapterDto(any(Chapter.class));
	}


	@Test
	public void testCreateChapter_CourseNotFound() {
		when(courseRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(CourseNotFoundException.class, () -> chapterService.createChapter(1L, createChapterDto));

		verify(courseRepository, times(1)).findById(1L);
		verify(chapterRepository, never()).save(any(Chapter.class));
	}


	@Test
	public void testUpdateChapter_Success() {
		when(chapterRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.of(chapter));
		when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);
		when(chapterMapper.mapToGetChapterDto(any(Chapter.class))).thenReturn(getChapterDto);

		GetChapterDto result = chapterService.updateChapter(1L, 1L, updateChapterDto);

		assertEquals(getChapterDto, result);
		assertEquals("Updated Chapter", chapter.getName());

		verify(chapterRepository, times(1)).findByCourseIdAndId(1L, 1L);
		verify(chapterRepository, times(1)).save(any(Chapter.class));
		verify(chapterMapper, times(1)).mapToGetChapterDto(any(Chapter.class));
	}


	@Test
	public void testUpdateChapter_NotFound() {
		when(chapterRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ChapterNotFoundException.class, () -> chapterService.updateChapter(1L, 1L, updateChapterDto));

		verify(chapterRepository, times(1)).findByCourseIdAndId(1L, 1L);
	}


	@Test
	public void testUpdateChapterOrder_Success() {
		Chapter chapter1 = new Chapter();
		chapter1.setId(1L);
		chapter1.setChapterOrder(1);
		Chapter chapter2 = new Chapter();
		chapter2.setId(2L);
		chapter2.setChapterOrder(2);
		Chapter chapter3 = new Chapter();
		chapter3.setId(3L);
		chapter3.setChapterOrder(3);
		Chapter chapter4 = new Chapter();
		chapter4.setId(4L);
		chapter4.setChapterOrder(4);

		List<Chapter> chapters = new ArrayList<>(List.of(chapter1, chapter2, chapter3, chapter4));

		when(chapterRepository.findAllByCourseId(1L)).thenReturn(chapters);
		when(chapterRepository.saveAll(anyList())).thenReturn(chapters);
		when(chapterMapper.mapToGetChapterDto(any(Chapter.class))).thenReturn(new GetChapterDto());

		GetChapterDto result = chapterService.updateChapterOrder(1L, 2L, 4);

		assertNotNull(result);

		assertEquals(1, chapter1.getChapterOrder());
		assertEquals(4, chapter2.getChapterOrder());
		assertEquals(2, chapter3.getChapterOrder());
		assertEquals(3, chapter4.getChapterOrder());


		verify(chapterRepository, times(1)).saveAll(anyList());
	}


	@Test
	public void testUpdateChapterOrder_OrderGreaterThanMax() {
		Chapter chapter2 = new Chapter();
		chapter2.setId(2L);
		chapter2.setChapterOrder(3);

		List<Chapter> chapters = new ArrayList<>(List.of(chapter, chapter2));

		when(chapterRepository.findAllByCourseId(1L)).thenReturn(chapters);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> chapterService.updateChapterOrder(1L, 1L, 4));

		assertEquals("New order number must be less than the greatest", exception.getMessage());
		verify(chapterRepository, never()).saveAll(anyList());
	}


	@Test
	public void testUpdateChapterOrder_OrderLessThanOrEqualToZero() {
		when(chapterRepository.findAllByCourseId(1L)).thenReturn(List.of(chapter));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> chapterService.updateChapterOrder(1L, 1L, 0));

		assertEquals("New order number must be greater than 0", exception.getMessage());
		verify(chapterRepository, never()).saveAll(anyList());
	}


	@Test
	public void testUpdateChapterOrder_ChapterNotFound() {
		Chapter chapter2 = new Chapter();
		chapter2.setId(2L);
		chapter2.setChapterOrder(2);

		when(chapterRepository.findAllByCourseId(1L)).thenReturn(new ArrayList<>(List.of(chapter2)));

		assertThrows(ChapterNotFoundException.class, () -> chapterService.updateChapterOrder(1L, 100L, 2));

		verify(chapterRepository, never()).saveAll(anyList());
	}


	@Test
	public void testDeleteChapter_Success() {
		when(chapterRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.of(chapter));

		chapterService.deleteChapter(1L, 1L);

		verify(chapterRepository, times(1)).findByCourseIdAndId(1L, 1L);
		verify(chapterRepository, times(1)).delete(any(Chapter.class));
	}


	@Test
	public void testDeleteChapter_NotFound() {
		when(chapterRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ChapterNotFoundException.class, () -> chapterService.deleteChapter(1L, 1L));

		verify(chapterRepository, times(1)).findByCourseIdAndId(1L, 1L);
		verify(chapterRepository, never()).delete(any(Chapter.class));
	}
}
