package kz.pryahin.bitlabInternship.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabInternship.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Long> {

	List<Lesson> findAllByChapterId(Long id);

	Optional<Lesson> findByChapterIdAndId(Long chapterId, Long lessonId);
}
