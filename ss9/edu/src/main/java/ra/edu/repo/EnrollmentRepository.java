package ra.edu.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.model.dto.response.CourseStatisticByStudents;
import ra.edu.model.dto.response.CourseStatisticByTeacher;
import ra.edu.model.dto.response.LessonEnrollmentCourseByStudent;
import ra.edu.model.entity.Enrollment;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    Optional<Enrollment> findByCourse_CourseId(long courseId);
    @Query("SELECT new ra.edu.model.dto.response.CourseStatisticByStudents(c.courseId,c.title,COUNT(e)) FROM Course c JOIN Enrollment e ON e.course = c GROUP BY c.courseId, c.title")
    Page<CourseStatisticByStudents> findCourseStatisticByStudents(Pageable pageable);
    @Query("SELECT new ra.edu.model.dto.response.LessonEnrollmentCourseByStudent(u.userId , c.courseId, c.title, e.progressPercentage) FROM Course c JOIN Enrollment e ON e.course = c JOIN User u ON e.student = u WHERE u.userId = :studentId GROUP BY u.userId, c.courseId, c.title,e.progressPercentage")
    Page<LessonEnrollmentCourseByStudent> findLessonEnrollmentCourseByStudents(@Param("studentId") Long studentId, Pageable pageable);
    @Query("SELECT new ra.edu.model.dto.response.CourseStatisticByTeacher(c.courseId,c.title,COUNT(e.student.userId),COUNT(l.lessonId),AVG(e.progressPercentage)) FROM Course c JOIN c.lessons l JOIN Enrollment e ON e.course = c WHERE c.teacher.userId = :teacherId GROUP BY c.courseId, c.title")
    Page<CourseStatisticByTeacher> findCourseStatisticByTeachers(@Param("teacherId") Long teacherId, Pageable pageable);

}
