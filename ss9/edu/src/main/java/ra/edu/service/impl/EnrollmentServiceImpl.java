package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.entity.*;
import ra.edu.repo.CourseRepository;
import ra.edu.repo.EnrollmentLessonRepository;
import ra.edu.repo.EnrollmentRepository;
import ra.edu.service.AuthService;
import ra.edu.service.CourseService;
import ra.edu.service.EnrollmentService;
import ra.edu.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AuthService authService;
    @Autowired
    private EnrollmentLessonRepository enrollmentLessonRepository;
    @Autowired
    private CourseRepository courseRepository;

    public Enrollment findById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new EntityNotFoundException("Enrollment with id " + enrollmentId + " not found"));
    }

    public ApiResponseData<Page<Enrollment>> enrollmentPage(int page, int size) {
        Page<Enrollment> enrollmentPage = enrollmentRepository.findAll(PageRequest.of(page, size));
        ApiResponseData<Page<Enrollment>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(enrollmentPage);
        apiResponseData.setMessage("enrollmentPage");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public ApiResponseData<Enrollment> createEnrollment(Enrollment enrollment, Long courseId, Long studentId) {
        ApiResponseData<Enrollment> apiResponseData = new ApiResponseData<>();
        Course course = courseService.findById(courseId);
        User user = userService.findByUserId(studentId);
        enrollment.setCourse(course);
        enrollment.setStudent(user);
        Enrollment enrollment1 = enrollmentRepository.save(enrollment);

        List<EnrollmentLesson> enrollmentLessonList = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            EnrollmentLesson enrollmentLesson = new EnrollmentLesson();
            enrollmentLesson.setLesson(lesson);
            enrollmentLesson.setEnrollment(enrollment1);
            enrollmentLessonList.add(enrollmentLesson);
        }
        enrollmentLessonRepository.saveAll(enrollmentLessonList);

        apiResponseData.setData(enrollment1);
        apiResponseData.setMessage("createEnrollment");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public ApiResponseData<Enrollment> getEnrollmentById(Long enrollmentId, Long studentID, Authentication authentication) {
        ApiResponseData<Enrollment> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if (!jwtResponseApiResponseData.getSuccess()) {
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setMessage("Can not get authentication");
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        JWTResponse jwtResponseUser = jwtResponseApiResponseData.getData();
        Long loginStudentId = jwtResponseUser.getId();
        if (loginStudentId.equals(studentID)) {
            apiResponseData.setMessage("Can't not see other student enrollment");
            apiResponseData.setSuccess(false);
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        Enrollment enrollment1 = findById(enrollmentId);
        apiResponseData.setData(enrollment1);
        apiResponseData.setMessage("getEnrollmentById");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public ApiResponseData<EnrollmentLesson> updateLessonCompleted(EnrollmentLesson enrollmentLesson, long courseId, Long lessonId, Authentication authentication) {
        Enrollment enrollment = enrollmentRepository.findByCourse_CourseId(courseId).orElseThrow(() -> new EntityNotFoundException("enrollment courseId"));
        ApiResponseData<EnrollmentLesson> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if (!jwtResponseApiResponseData.getSuccess()) {
            apiResponseData.setMessage("Unauthorized");
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }

        JWTResponse jwtUser = jwtResponseApiResponseData.getData();
        if (!enrollment.getStudent().getUserId().equals(jwtUser.getId())) {
            apiResponseData.setMessage("You are not the student of this enrollment");
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.FORBIDDEN);
            return apiResponseData;
        }

        EnrollmentLesson lesson = enrollmentLessonRepository.findByEnrollment_EnrollmentIdAndLesson_LessonId(enrollment.getEnrollmentId(), lessonId).orElseThrow(() -> new EntityNotFoundException("enrollment lessonId"));
        enrollmentLesson.setCompleted(true);
        enrollmentLesson.setCompletedAt(LocalDateTime.now());
        enrollmentLessonRepository.save(enrollmentLesson);

        Long totalLessons = enrollmentLessonRepository.countByEnrollment_EnrollmentId(enrollment.getEnrollmentId());
        Long completedLessons = enrollmentLessonRepository.countByEnrollment_EnrollmentIdAndCompletedTrue(lessonId);

        Double progress = (double) (totalLessons / completedLessons * 100);
        enrollment.setProgressPercentage(progress);
        if (completedLessons == totalLessons) {
            enrollment.setStatus("COMPLETED");
            enrollment.setCompletionDate(LocalDateTime.now());
        }
        enrollmentRepository.save(enrollment);
        apiResponseData.setMessage("Lesson completed successfully");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }
}
