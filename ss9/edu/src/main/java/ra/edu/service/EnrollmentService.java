package ra.edu.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentLesson;

public interface EnrollmentService {
    Enrollment findById(Long enrollmentId);
    ApiResponseData<Page<Enrollment>> enrollmentPage(int page, int size);
    ApiResponseData<Enrollment> createEnrollment(Enrollment enrollment, Long courseId, Long studentId);
    ApiResponseData<Enrollment> getEnrollmentById(Long enrollmentId, Long studentID, Authentication authentication);
    ApiResponseData<EnrollmentLesson> updateLessonCompleted(EnrollmentLesson enrollmentLesson, long courseId, Long lessonId, Authentication authentication);
}
