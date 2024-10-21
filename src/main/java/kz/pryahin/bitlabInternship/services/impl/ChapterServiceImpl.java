package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.dtos.chapterDtos.CreateChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.GetChapterDto;
import kz.pryahin.bitlabInternship.dtos.chapterDtos.UpdateChapterDto;
import kz.pryahin.bitlabInternship.entities.Chapter;
import kz.pryahin.bitlabInternship.entities.Course;
import kz.pryahin.bitlabInternship.exceptions.BlankNameException;
import kz.pryahin.bitlabInternship.exceptions.ChapterNotFoundException;
import kz.pryahin.bitlabInternship.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.mapper.ChapterMapper;
import kz.pryahin.bitlabInternship.repositories.ChapterRepository;
import kz.pryahin.bitlabInternship.repositories.CourseRepository;
import kz.pryahin.bitlabInternship.services.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
	private final ChapterRepository chapterRepository;
	private final ChapterMapper chapterMapper;
	private final CourseRepository courseRepository;


	@Override
	public List<GetChapterDto> getAllChapters() {
		return chapterRepository.findAll().stream()
			.map(chapterMapper::mapToGetChapterDto)
			.toList();
	}


	@Override
	public List<GetChapterDto> getAllChaptersByCourseId(Long courseId) {
		return chapterRepository.findAllByCourseId(courseId).stream()
			.map(chapterMapper::mapToGetChapterDto)
			.toList();
	}


	@Override
	public GetChapterDto getChapterById(Long courseId, Long chapterId) {
		return chapterMapper.mapToGetChapterDto(chapterRepository.findByCourseIdAndId(courseId, chapterId).orElseThrow(ChapterNotFoundException::new));
	}


	@Override
	public GetChapterDto createChapter(Long courseId, CreateChapterDto createChapterDto) {
		Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
		Chapter chapter = chapterMapper.mapCreateChapterDtoToEntity(createChapterDto);

		chapter.setCourse(course);

		List<Chapter> chapters = chapterRepository.findAllByCourseId(courseId);

		Optional<Chapter> chapterWithMaxOrderValue =
			chapters.stream()
				.max(Comparator.comparing(Chapter::getChapterOrder));


		if (chapterWithMaxOrderValue.isPresent()) {
			chapter.setChapterOrder(chapterWithMaxOrderValue.get().getChapterOrder() + 1);
		} else {
			chapter.setChapterOrder(1);
		}

		return chapterMapper.mapToGetChapterDto(chapterRepository.save(chapter));
	}


	@Override
	public GetChapterDto updateChapter(Long courseId, Long chapterId, UpdateChapterDto updateChapterDto) {
		Chapter chapter =
			chapterRepository.findByCourseIdAndId(courseId, chapterId).orElseThrow(ChapterNotFoundException::new);

		if (updateChapterDto.getName() != null) {
			if (!updateChapterDto.getName().isBlank()) {
				chapter.setName(updateChapterDto.getName());
			} else {
				throw new BlankNameException();
			}
		}

		if (updateChapterDto.getDescription() != null) {
			chapter.setDescription(updateChapterDto.getDescription());
		}

		if (updateChapterDto.getCourseId() != null) {
			Course course = courseRepository.findById(updateChapterDto.getCourseId()).orElseThrow(()
				-> new CourseNotFoundException("The Course to which you are adding the Chapter is not found"));
			chapter.setCourse(course);

			List<Chapter> chapters = chapterRepository.findAllByCourseId(updateChapterDto.getCourseId());
			Optional<Chapter> chapterWithMaxOrderValue = chapters.stream()
				.max(Comparator.comparing(Chapter::getChapterOrder));

			if (chapterWithMaxOrderValue.isPresent()) {
				chapter.setChapterOrder(chapterWithMaxOrderValue.get().getChapterOrder() + 1);
			} else {
				chapter.setChapterOrder(1);
			}
		}

		return chapterMapper.mapToGetChapterDto(chapterRepository.save(chapter));
	}


	@Override
	public GetChapterDto updateChapterOrder(Long courseId, Long chapterId, int chapterOrder) {
		List<Chapter> chapters = chapterRepository.findAllByCourseId(courseId);
		Optional<Chapter> chapterWithMaxOrder = chapters.stream().max(Comparator.comparing(Chapter::getChapterOrder));

		if (chapterWithMaxOrder.isPresent()) {
			if (chapterOrder > chapterWithMaxOrder.get().getChapterOrder()) {
				throw new IllegalArgumentException("New order number must be less than the greatest");
			}
			if (chapterOrder <= 0) {
				throw new IllegalArgumentException("New order number must be greater than 0");
			}
		}

		chapters.sort(Comparator.comparing(Chapter::getChapterOrder));

		// Находим Chapter который хотим изменить
		Chapter chapterToChange = chapters.stream()
			.filter(chapter -> chapter.getId().equals(chapterId))
			.findFirst()
			.orElseThrow(ChapterNotFoundException::new);

		// Сохраняем порядковый номер еще до изменений
		int oldOrder = chapterToChange.getChapterOrder();

		// Если новый номер больше старого
		if (chapterOrder > oldOrder) {
			chapters.stream()
				.filter(chapter -> chapter.getChapterOrder() > oldOrder && chapter.getChapterOrder() <= chapterOrder)
				.forEach(chapter -> chapter.setChapterOrder(chapter.getChapterOrder() - 1));
			// Если новый номер меньше старого
		} else if (chapterOrder < oldOrder) {
			chapters.stream()
				.filter(chapter -> chapter.getChapterOrder() >= chapterOrder && chapter.getChapterOrder() < oldOrder)
				.forEach(chapter -> chapter.setChapterOrder(chapter.getChapterOrder() + 1));
		}

		chapterToChange.setChapterOrder(chapterOrder);

		chapterRepository.saveAll(chapters);

		return chapterMapper.mapToGetChapterDto(chapterToChange);

	}


	@Override
	public void deleteChapter(Long courseId, Long chapterId) {
		Chapter chapter =
			chapterRepository.findByCourseIdAndId(courseId, chapterId).orElseThrow(ChapterNotFoundException::new);
		chapterRepository.delete(chapter);
	}
}
