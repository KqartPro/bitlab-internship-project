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
import kz.pryahin.bitlabInternship.services.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {
	private final ChapterRepository chapterRepository;
	private final ChapterMapper chapterMapper;
	private final CourseRepository courseRepository;


	@Override
	public List<GetChapterDto> getAllChapters() {
		log.info("Вызван метод getAllChapters");
		List<Chapter> chapters = chapterRepository.findAll();
		log.debug("Найдено {} глав", chapters.size());

		log.info("Метод getAllChapters выполнен");
		return chapters.stream()
			.map(chapterMapper::mapToGetChapterDto)
			.toList();
	}


	@Override
	public List<GetChapterDto> getAllChaptersByCourseId(Long courseId) {
		log.info("Вызван метод getAllChaptersByCourseId с ID курса: {}", courseId);

		List<Chapter> chapters = chapterRepository.findAllByCourseId(courseId);
		chapters.sort(Comparator.comparing(Chapter::getChapterOrder));
		log.debug("Найдено {} глав для курса с ID {}", chapters.size(), courseId);

		log.info("Метод getAllChaptersByCourseId выполнен");
		return chapters.stream()
			.map(chapterMapper::mapToGetChapterDto)
			.toList();
	}


	@Override
	public GetChapterDto getChapterById(Long courseId, Long chapterId) {
		log.info("Вызван метод getChapterById с ID курса: {} и ID главы: {}", courseId, chapterId);

		Chapter chapter = chapterRepository.findByCourseIdAndId(courseId, chapterId)
			.orElseThrow(() -> {
				log.error("Глава с ID {} для курса с ID {} не найдена", chapterId, courseId);
				return new ChapterNotFoundException();
			});
		log.debug("Глава найдена: {}", chapter);

		log.info("Метод getChapterById выполнен");
		return chapterMapper.mapToGetChapterDto(chapter);
	}


	@Override
	public GetChapterDto createChapter(Long courseId, CreateChapterDto createChapterDto) {
		log.info("Вызван метод createChapter для курса с ID: {}", courseId);

		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> {
				log.error("Курс с ID {} не найден", courseId);
				return new CourseNotFoundException();
			});

		log.debug("Делаем mapping CreateChapterDto в Chapter");
		Chapter chapter = chapterMapper.mapCreateChapterDtoToEntity(createChapterDto);

		log.debug("Добавляем найденный Course в Chapter");
		chapter.setCourse(course);

		log.debug("Сохраняем все chapters в List<Chapter> chapters из БД");
		List<Chapter> chapters = chapterRepository.findAllByCourseId(courseId);

		log.debug("Находим chapter с максимальным значением поля chapterOrder");
		Optional<Chapter> chapterWithMaxOrderValue = chapters.stream()
			.max(Comparator.comparing(Chapter::getChapterOrder));

		log.debug("Устанавливаем значение для поля chapter.chapterOrder");
		if (chapterWithMaxOrderValue.isPresent()) {
			chapter.setChapterOrder(chapterWithMaxOrderValue.get().getChapterOrder() + 1);
			log.debug("Установлен порядок главы: {}", chapter.getChapterOrder());
		} else {
			chapter.setChapterOrder(1);
			log.debug("Установлен порядок главы по умолчанию: 1");
		}

		Chapter savedChapter = chapterRepository.save(chapter);

		log.debug("Глава успешно создана с ID: {}", savedChapter.getId());

		log.info("Метод createChapter выполнен");
		return chapterMapper.mapToGetChapterDto(savedChapter);
	}


	@Override
	public GetChapterDto updateChapter(Long courseId, Long chapterId, UpdateChapterDto updateChapterDto) {
		log.info("Вызван метод updateChapter для курса с ID: {} и главы с ID: {}", courseId, chapterId);

		Chapter chapter = chapterRepository.findByCourseIdAndId(courseId, chapterId)
			.orElseThrow(() -> {
				log.error("Глава с ID {} для курса с ID {} не найдена", chapterId, courseId);
				return new ChapterNotFoundException();
			});

		if (updateChapterDto.getName() != null && !updateChapterDto.getName().isBlank()) {
			chapter.setName(updateChapterDto.getName());
			log.debug("Имя главы обновлено: {}", updateChapterDto.getName());
		}

		if (updateChapterDto.getDescription() != null) {
			chapter.setDescription(updateChapterDto.getDescription());
			log.debug("Описание главы обновлено");
		}

		if (updateChapterDto.getCourseId() != null) {
			Course course = courseRepository.findById(updateChapterDto.getCourseId())
				.orElseThrow(() -> {
					log.error("Курс с ID {} не найден", updateChapterDto.getCourseId());
					return new CourseNotFoundException();
				});
			chapter.setCourse(course);
			log.debug("Курс главы обновлен на курс с ID {}", updateChapterDto.getCourseId());
		}

		Chapter updatedChapter = chapterRepository.save(chapter);

		log.info("Глава успешно обновлена с ID: {}", updatedChapter.getId());
		return chapterMapper.mapToGetChapterDto(updatedChapter);
	}


	@Override
	public GetChapterDto updateChapterOrder(Long courseId, Long chapterId, int chapterOrder) {
		log.info("Вызван метод updateChapterOrder для Course ID: {}, Chapter ID: {}, ChapterOrder: {}", courseId,
			chapterId, chapterOrder);

		List<Chapter> chapters = chapterRepository.findAllByCourseId(courseId);
		Optional<Chapter> chapterWithMaxOrder = chapters.stream()
			.max(Comparator.comparing(Chapter::getChapterOrder));

		if (chapterWithMaxOrder.isPresent()) {
			if (chapterOrder > chapterWithMaxOrder.get().getChapterOrder()) {
				log.error("Новый порядок {} больше максимального порядка {}", chapterOrder, chapterWithMaxOrder.get().getChapterOrder());
				throw new IllegalArgumentException("New order number must be less than the greatest");
			}
			if (chapterOrder <= 0) {
				log.error("Новый порядок {} меньше или равен 0", chapterOrder);
				throw new IllegalArgumentException("New order number must be greater than 0");
			}
		}

		log.debug("Начальная сортировка глав по порядку");
		chapters.sort(Comparator.comparing(Chapter::getChapterOrder));

		Chapter chapterToChange = chapters.stream()
			.filter(chapter -> chapter.getId().equals(chapterId))
			.findFirst()
			.orElseThrow(() -> {
				log.error("Глава с ID {} не найдена в курсе с ID {}", chapterId, courseId);
				return new ChapterNotFoundException();
			});

		int oldOrder = chapterToChange.getChapterOrder();
		log.debug("Текущий порядок главы: {}, Новый порядок: {}", oldOrder, chapterOrder);

		if (chapterOrder > oldOrder) {
			log.debug("Новый порядок больше старого, сдвиг остальных глав назад");
			chapters.stream()
				.filter(chapter -> chapter.getChapterOrder() > oldOrder && chapter.getChapterOrder() <= chapterOrder)
				.forEach(chapter -> chapter.setChapterOrder(chapter.getChapterOrder() - 1));
		} else if (chapterOrder < oldOrder) {
			log.debug("Новый порядок меньше старого, сдвиг остальных глав вперед");
			chapters.stream()
				.filter(chapter -> chapter.getChapterOrder() >= chapterOrder && chapter.getChapterOrder() < oldOrder)
				.forEach(chapter -> chapter.setChapterOrder(chapter.getChapterOrder() + 1));
		}

		chapterToChange.setChapterOrder(chapterOrder);
		log.debug("Обновлен порядок главы с ID {} на {}", chapterId, chapterOrder);

		chapterRepository.saveAll(chapters);

		log.info("Метод updateChapterOrder выполнен успешно");
		return chapterMapper.mapToGetChapterDto(chapterToChange);
	}


	@Override
	public void deleteChapter(Long courseId, Long chapterId) {
		log.info("Вызван метод deleteChapter для курса с ID: {} и главы с ID: {}", courseId, chapterId);

		Chapter chapter = chapterRepository.findByCourseIdAndId(courseId, chapterId)
			.orElseThrow(() -> {
				log.error("Глава с ID {} для курса с ID {} не найдена", chapterId, courseId);
				return new ChapterNotFoundException();
			});
		chapterRepository.delete(chapter);
		log.info("Метод deleteChapter выполнен. Глава с ID {} успешно удалена", chapterId);
	}
}
