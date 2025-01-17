package kz.pryahin.bitlabInternship.main.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabInternship.main.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
  List<Chapter> findAllByCourseId(Long courseId);

  Optional<Chapter> findByCourseIdAndId(Long courseId, Long chapterId);

}

