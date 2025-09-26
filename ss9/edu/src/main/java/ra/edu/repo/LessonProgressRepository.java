package ra.edu.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.LessonProgress;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Long countByLesson_LessonId(Long lessonId);

    Long countByLesson_LessonIdAndIdCompletedTrue(Long lessonId);
}
