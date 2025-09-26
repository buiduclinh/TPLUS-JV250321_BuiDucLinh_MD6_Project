package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.*;
import ra.edu.model.entity.*;
import ra.edu.repo.CourseRepository;
import ra.edu.repo.EnrollmentRepository;
import ra.edu.repo.LessonProgressRepository;
import ra.edu.service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private LessonService lessonService;

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

        List<LessonProgress> lessonProgressList = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            LessonProgress lessonProgress = new LessonProgress();
            lessonProgress.setLesson(lesson);
            lessonProgress.setEnrollment(enrollment1);
            lessonProgressList.add(lessonProgress);
        }
        lessonProgressRepository.saveAll(lessonProgressList);

        apiResponseData.setData(enrollment1);
        apiResponseData.setMessage("createEnrollment");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public ApiResponseData<Enrollment> getEnrollmentById(Long enrollmentId, Long studentID) {
        ApiResponseData<Enrollment> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser();
        if (!jwtResponseApiResponseData.getSuccess()) {
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setMessage("Can not get authentication");
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        JWTResponse jwtResponseUser = jwtResponseApiResponseData.getData();
        Long loginStudentId = jwtResponseUser.getId();
        if (!loginStudentId.equals(studentID)) {
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

    public ApiResponseData<LessonProgress> updateLessonCompleted(Long enrollmentId , Long lessonId ,LessonProgress lessonProgress) {
        ApiResponseData<LessonProgress> apiResponseData = new ApiResponseData<>();
        Enrollment enrollment = findById(enrollmentId);
        Lesson lesson = lessonService.lessonById(lessonId);
        lessonProgress.setEnrollment(enrollment);
        lessonProgress.setLesson(lesson);
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser();
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

        lessonProgress.setIdCompleted(true);
        lessonProgress.setCompletedAt(LocalDateTime.now());
        lessonProgressRepository.save(lessonProgress);

        Long totalLessons = lessonProgressRepository.countByLesson_LessonId(lessonProgress.getLesson().getLessonId());
        Long completedLessons = lessonProgressRepository.countByLesson_LessonIdAndIdCompletedTrue(lessonProgress.getLesson().getLessonId());

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

    public ApiResponseData<Page<CourseStatisticByStudents>> courseStatisticByStudents(int page, int size){
        Page<CourseStatisticByStudents> courseStatisticByStudents = enrollmentRepository.findCourseStatisticByStudents(PageRequest.of(page, size));
        ApiResponseData<Page<CourseStatisticByStudents>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(Boolean.TRUE);
        apiResponseData.setErrors(null);
        apiResponseData.setData(courseStatisticByStudents);
        apiResponseData.setMessage("course statistics found");
        return apiResponseData;
    }

    public ApiResponseData<Page<LessonEnrollmentCourseByStudent>> lessonEnrollmentCourseByStudents(Long studentId, int page, int size){
        User student = userService.findByUserId(studentId);
        Page<LessonEnrollmentCourseByStudent> lessonEnrollmentCourseByStudentPage = enrollmentRepository.findLessonEnrollmentCourseByStudents(student.getUserId(), PageRequest.of(page, size));
        ApiResponseData<Page<LessonEnrollmentCourseByStudent>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(Boolean.TRUE);
        apiResponseData.setErrors(null);
        apiResponseData.setData(lessonEnrollmentCourseByStudentPage);
        apiResponseData.setMessage("lesson enrollment course found");
        return apiResponseData;
    }

    public ApiResponseData<Page<CourseStatisticByTeacher>> courseStatisticByTeacher(int page, int size, Long teacherId){
        User teacher = userService.findByUserId(teacherId);
        Page<CourseStatisticByTeacher> statisticByTeachers = enrollmentRepository.findCourseStatisticByTeachers(teacher.getUserId(), PageRequest.of(page, size));
        ApiResponseData<Page<CourseStatisticByTeacher>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(Boolean.TRUE);
        apiResponseData.setErrors(null);
        apiResponseData.setData(statisticByTeachers);
        apiResponseData.setMessage("course statistics found");
        return apiResponseData;
    }
}
