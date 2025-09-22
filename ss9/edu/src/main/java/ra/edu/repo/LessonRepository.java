package ra.edu.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.Lesson;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l JOIN l.course c WHERE c.status = :status")
    Page<Lesson> findAllByCourseStatus(@Param("status") String status, Pageable pageable);
    @Query("SELECT l FROM Lesson l JOIN l.course c WHERE c.status = :status AND l.lessonId = :id")
    Optional<Lesson> findByCourseStatusAndLessonId(@Param("status") String status, @Param("id") Long id);
}
