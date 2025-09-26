package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.CourseStatisticByStudents;
import ra.edu.model.dto.response.CourseStatisticByTeacher;
import ra.edu.model.dto.response.LessonEnrollmentCourseByStudent;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.LessonProgress;

public interface EnrollmentService {
    Enrollment findById(Long enrollmentId);
    ApiResponseData<Page<Enrollment>> enrollmentPage(int page, int size);
    ApiResponseData<Enrollment> createEnrollment(Enrollment enrollment, Long courseId, Long studentId);
    ApiResponseData<Enrollment> getEnrollmentById(Long enrollmentId, Long studentID);
    ApiResponseData<LessonProgress> updateLessonCompleted(Long enrollmentId , Long lessonId , LessonProgress lessonProgress);
    ApiResponseData<Page<CourseStatisticByStudents>> courseStatisticByStudents(int page, int size);
    ApiResponseData<Page<LessonEnrollmentCourseByStudent>> lessonEnrollmentCourseByStudents(Long studentId, int page, int size);
    ApiResponseData<Page<CourseStatisticByTeacher>> courseStatisticByTeacher(int page, int size, Long teacherId);
}
