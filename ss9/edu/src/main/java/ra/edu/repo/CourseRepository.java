package ra.edu.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE (:status IS NULL OR :status = c.status) ")
    Page<Course> findAll(Pageable pageable,  @Param("status") String status);

    Page<Course> findCourseByTitleContainingIgnoreCase(String courseName,Pageable pageable);

    Page<Course> findCourseByTeacher_UserId(Long teacherId,Pageable pageable);
}
