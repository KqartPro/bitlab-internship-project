package kz.pryahin.bitlabInternship.main.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabInternship.main.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {
}
