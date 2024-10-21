package kz.pryahin.bitlabInternship.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabInternship.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {
}
