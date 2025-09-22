package ra.edu.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.EnrollmentLesson;

import java.util.Optional;

@Repository
public interface EnrollmentLessonRepository extends JpaRepository<EnrollmentLesson,Long> {
    Optional<EnrollmentLesson> findByEnrollment_EnrollmentIdAndLesson_LessonId(Long enrollmentId, Long lessonId);

    Long countByEnrollment_EnrollmentId(Long enrollmentId);

    Long countByEnrollment_EnrollmentIdAndCompletedTrue(Long lessonId);
}
