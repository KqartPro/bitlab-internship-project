package kz.pryahin.bitlabInternship.services.impl;

import kz.pryahin.bitlabInternship.dtos.lessonDtos.CreateLessonDto;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.GetLessonDto;
import kz.pryahin.bitlabInternship.dtos.lessonDtos.UpdateLessonDto;
import kz.pryahin.bitlabInternship.entities.Chapter;
import kz.pryahin.bitlabInternship.entities.Course;
import kz.pryahin.bitlabInternship.entities.Lesson;
import kz.pryahin.bitlabInternship.exceptions.BlankNameException;
import kz.pryahin.bitlabInternship.exceptions.ChapterNotFoundException;
import kz.pryahin.bitlabInternship.exceptions.CourseNotFoundException;
import kz.pryahin.bitlabInternship.exceptions.LessonNotFoundException;
import kz.pryahin.bitlabInternship.mapper.LessonMapper;
import kz.pryahin.bitlabInternship.repositories.ChapterRepository;
import kz.pryahin.bitlabInternship.repositories.CourseRepository;
import kz.pryahin.bitlabInternship.repositories.LessonRepository;
import kz.pryahin.bitlabInternship.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
	private final LessonRepository lessonRepository;
	private final LessonMapper lessonMapper;
	private final ChapterRepository chapterRepository;
	private final CourseRepository courseRepository;


	private void isCourseContainsChapter(Long courseId, Long chapterId) {
		Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(ChapterNotFoundException::new);

		Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);

		if (!chapter.getCourse().getId().equals(courseId)) {
			throw new IllegalArgumentException("This course doesn't contain the requested chapter");
		}
	}


	@Override
	public List<GetLessonDto> getAllLessons() {
		return lessonRepository.findAll().stream()
			.map(lessonMapper::mapToGetLessonDto)
			.toList();
	}


	@Override
	public List<GetLessonDto> getAllLessonsFromChapter(Long courseId, Long chapterId) {
		isCourseContainsChapter(courseId, chapterId);

		return lessonRepository.findAllByChapterId(chapterId).stream()
			.map(lessonMapper::mapToGetLessonDto)
			.toList();
	}


	@Override
	public GetLessonDto getLessonById(Long courseId, Long chapterId, Long lessonId) {
		isCourseContainsChapter(courseId, chapterId);

		return lessonMapper.mapToGetLessonDto(lessonRepository.findByChapterIdAndId(chapterId, lessonId).orElseThrow(LessonNotFoundException::new));
	}


	@Override
	public GetLessonDto createLesson(Long courseId, Long chapterId, CreateLessonDto createLessonDto) {
		isCourseContainsChapter(courseId, chapterId);

		Lesson lesson = lessonMapper.mapCreateLessonDtoToEntity(createLessonDto);
		Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(ChapterNotFoundException::new);

		lesson.setChapter(chapter);

		List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);

		Optional<Lesson> lessonWithMaxOrderValue =
			lessons.stream()
				.max(Comparator.comparing(Lesson::getLessonOrder));

		if (lessonWithMaxOrderValue.isPresent()) {
			lesson.setLessonOrder(lessonWithMaxOrderValue.get().getLessonOrder() + 1);
		} else {
			lesson.setLessonOrder(1);
		}

		return lessonMapper.mapToGetLessonDto(lessonRepository.save(lesson));
	}


	@Override
	public GetLessonDto updateLesson(Long courseId, Long chapterId, Long lessonId, UpdateLessonDto updateLessonDto) {
		isCourseContainsChapter(courseId, chapterId);

		Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(LessonNotFoundException::new);

		if (updateLessonDto.getName() != null) {
			if (!updateLessonDto.getName().isBlank()) {
				lesson.setName(updateLessonDto.getName());
			} else {
				throw new BlankNameException();
			}
		}

		if (updateLessonDto.getDescription() != null) {
			lesson.setDescription(updateLessonDto.getDescription());
		}

		if (updateLessonDto.getContent() != null) {
			lesson.setContent(updateLessonDto.getContent());
		}

		if (updateLessonDto.getChapterId() != null) {
			Chapter chapter =
				chapterRepository.findById(updateLessonDto.getChapterId()).orElseThrow(() -> new ChapterNotFoundException(
					"The chapter to which you are adding the Lesson is not found"));

			List<Lesson> lessons = lessonRepository.findAllByChapterId(updateLessonDto.getChapterId());
			Optional<Lesson> lessonWithMaxOrder = lessons.stream().max(Comparator.comparing(Lesson::getLessonOrder));

			if (lessonWithMaxOrder.isPresent()) {
				lesson.setLessonOrder(lessonWithMaxOrder.get().getLessonOrder() + 1);
			} else {
				lesson.setLessonOrder(1);
			}

			lesson.setChapter(chapter);
		}

		return lessonMapper.mapToGetLessonDto(lessonRepository.save(lesson));
	}


	@Override
	public GetLessonDto updateLessonOrder(Long courseId, Long chapterId, Long lessonId, int lessonOrder) {
		isCourseContainsChapter(courseId, chapterId);

		List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);
		Optional<Lesson> lessonWithMaxOrder = lessons.stream().max(Comparator.comparing(Lesson::getLessonOrder));

		if (lessonWithMaxOrder.isPresent()) {
			if (lessonOrder > lessonWithMaxOrder.get().getLessonOrder()) {
				throw new IllegalArgumentException("New order number must be less than the greatest");
			}
			if (lessonOrder <= 0) {
				throw new IllegalArgumentException("New order number must be greater than 0");
			}
		}

		lessons.sort(Comparator.comparing(Lesson::getLessonOrder));

		// Находим Lesson который хотим изменить
		Lesson lessonToChange = lessons.stream()
			.filter(lesson -> lesson.getId().equals(lessonId))
			.findFirst()
			.orElseThrow(LessonNotFoundException::new);

		// Сохраняем порядковый номер еще до изменений
		int oldOrder = lessonToChange.getLessonOrder();

		// Если новый номер больше старого
		if (lessonOrder > oldOrder) {
			lessons.stream()
				.filter(lesson -> lesson.getLessonOrder() > oldOrder && lesson.getLessonOrder() <= lessonOrder)
				.forEach(lesson -> lesson.setLessonOrder(lesson.getLessonOrder() - 1));
			// Если новый номер меньше старого
		} else if (lessonOrder < oldOrder) {
			lessons.stream()
				.filter(lesson -> lesson.getLessonOrder() >= lessonOrder && lesson.getLessonOrder() < oldOrder)
				.forEach(lesson -> lesson.setLessonOrder(lesson.getLessonOrder() + 1));
		}

		lessonToChange.setLessonOrder(lessonOrder);

		lessonRepository.saveAll(lessons);

		return lessonMapper.mapToGetLessonDto(lessonToChange);

	}


	@Override
	public void deleteLesson(Long courseId, Long chapterId, Long lessonId) {
		isCourseContainsChapter(courseId, chapterId);

		Lesson lesson =
			lessonRepository.findByChapterIdAndId(chapterId, lessonId).orElseThrow(LessonNotFoundException::new);
		lessonRepository.delete(lesson);
	}
}



