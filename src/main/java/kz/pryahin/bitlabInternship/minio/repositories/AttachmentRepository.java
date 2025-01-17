package kz.pryahin.bitlabInternship.minio.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabInternship.minio.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByLessonId(Long lessonId);
}
