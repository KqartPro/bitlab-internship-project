package kz.pryahin.bitlabInternship.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.UpdateChapterDto;
import kz.pryahin.bitlabInternship.services.ChapterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChapterController.class)
class ChapterControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ChapterService chapterService;


	@Test
	void getAllChapters() throws Exception {
		GetChapterDto chapter1 = new GetChapterDto();
		chapter1.setId(1L);
		chapter1.setName("Chapter 1");
		chapter1.setDescription("Description");
		chapter1.setChapterOrder(1);

		GetChapterDto chapter2 = new GetChapterDto();
		chapter2.setId(2L);
		chapter2.setName("Chapter 2");
		chapter2.setDescription("Description");
		chapter2.setChapterOrder(2);

		when(chapterService.getAllChapters()).thenReturn(List.of(chapter1, chapter2));

		mockMvc.perform(get("/chapter/get-all"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("Chapter 1"))
			.andExpect(jsonPath("$[1].name").value("Chapter 2"));

		verify(chapterService, times(1)).getAllChapters();
	}


	@Test
	void getAllChaptersByCourseId() throws Exception {
		Long courseId = 1L;
		GetChapterDto chapter1 = new GetChapterDto();
		chapter1.setId(1L);
		chapter1.setName("Chapter 1");
		chapter1.setDescription("Description");
		chapter1.setChapterOrder(1);

		GetChapterDto chapter2 = new GetChapterDto();
		chapter2.setId(2L);
		chapter2.setName("Chapter 2");
		chapter2.setDescription("Description");
		chapter2.setChapterOrder(2);

		when(chapterService.getAllChaptersByCourseId(courseId)).thenReturn(List.of(chapter1, chapter2));

		mockMvc.perform(get("/chapter/get-all-from-course/{courseId}", courseId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("Chapter 1"))
			.andExpect(jsonPath("$[1].name").value("Chapter 2"));

		verify(chapterService, times(1)).getAllChaptersByCourseId(courseId);
	}


	@Test
	void getChapterById() throws Exception {
		Long courseId = 1L;
		Long chapterId = 1L;
		GetChapterDto chapter = new GetChapterDto();
		chapter.setId(1L);
		chapter.setName("Chapter 1");
		chapter.setDescription("Description");
		chapter.setChapterOrder(1);

		when(chapterService.getChapterById(courseId, chapterId)).thenReturn(chapter);

		mockMvc.perform(get("/chapter/get-one-chapter")
				.param("courseId", courseId.toString())
				.param("chapterId", chapterId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Chapter 1"));

		verify(chapterService, times(1)).getChapterById(courseId, chapterId);
	}


	@Test
	void createChapter() throws Exception {
		Long courseId = 1L;
		CreateChapterDto createDto = new CreateChapterDto();
		createDto.setName("Chapter 1");
		createDto.setDescription("Description");


		GetChapterDto responseDto = new GetChapterDto();
		responseDto.setId(1L);
		responseDto.setName("Chapter 1");
		responseDto.setDescription("Description");
		responseDto.setChapterOrder(1);

		when(chapterService.createChapter(eq(courseId), any(CreateChapterDto.class))).thenReturn(responseDto);

		mockMvc.perform(post("/chapter/create/{courseId}", courseId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value("Chapter 1"));

		verify(chapterService, times(1)).createChapter(eq(courseId), any(CreateChapterDto.class));
	}


	@Test
	void updateChapter() throws Exception {
		Long courseId = 1L;
		Long chapterId = 1L;
		UpdateChapterDto updateDto = new UpdateChapterDto();
		updateDto.setName("Updated Chapter");
		updateDto.setDescription("Updated Description");

		GetChapterDto responseDto = new GetChapterDto();
		responseDto.setId(1L);
		responseDto.setName("Updated Chapter");
		responseDto.setDescription("Updated Description");
		responseDto.setChapterOrder(2);

		when(chapterService.updateChapter(eq(courseId), eq(chapterId), any(UpdateChapterDto.class))).thenReturn(responseDto);

		mockMvc.perform(patch("/chapter/update")
				.param("courseId", courseId.toString())
				.param("chapterId", chapterId.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Updated Chapter"));

		verify(chapterService, times(1)).updateChapter(eq(courseId), eq(chapterId), any(UpdateChapterDto.class));
	}


	@Test
	void updateChapterOrder() throws Exception {
		Long courseId = 1L;
		Long chapterId = 1L;
		int newOrder = 2;
		GetChapterDto responseDto = new GetChapterDto();
		responseDto.setId(1L);
		responseDto.setName("Chapter 1");
		responseDto.setDescription("Description");
		responseDto.setChapterOrder(newOrder);

		when(chapterService.updateChapterOrder(courseId, chapterId, newOrder)).thenReturn(responseDto);

		mockMvc.perform(put("/chapter/update-order")
				.param("courseId", courseId.toString())
				.param("chapterId", chapterId.toString())
				.param("chapterOrder", Integer.toString(newOrder)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chapterOrder").value(newOrder));

		verify(chapterService, times(1)).updateChapterOrder(courseId, chapterId, newOrder);
	}


	@Test
	void deleteChapter() throws Exception {
		Long courseId = 1L;
		Long chapterId = 1L;

		doNothing().when(chapterService).deleteChapter(courseId, chapterId);

		mockMvc.perform(delete("/chapter/delete")
				.param("courseId", courseId.toString())
				.param("chapterId", chapterId.toString()))
			.andExpect(status().isOk());

		verify(chapterService, times(1)).deleteChapter(courseId, chapterId);
	}
}
