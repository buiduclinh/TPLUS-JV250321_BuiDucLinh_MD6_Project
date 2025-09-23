package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;
import ra.edu.repo.LessonRepository;
import ra.edu.service.AuthService;
import ra.edu.service.CourseService;
import ra.edu.service.LessonService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private CourseService courseService;

    public Lesson lessonById(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("lessonId"));
    }

    public ApiResponseData<Lesson> findById(Long lessonId, String courseStatus) {
        ApiResponseData<Lesson> apiResponseData = new ApiResponseData<>();
        Optional<Lesson> lesson = lessonRepository.findByCourseStatusAndLessonId(courseStatus, lessonId);
        if (lesson.isPresent()) {
            apiResponseData.setData(lesson.get());
            apiResponseData.setMessage("lesson");
            apiResponseData.setErrors(null);
            apiResponseData.setSuccess(true);
            apiResponseData.setStatus(HttpStatus.OK);
            return apiResponseData;
        }
        apiResponseData.setMessage("lesson");
        apiResponseData.setData(null);
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<Lesson> createLesson(Lesson lesson, Long courseId, Authentication authentication) {
        ApiResponseData<Lesson> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if(!jwtResponseApiResponseData.getSuccess()){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("Cannot create lesson");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }

        JWTResponse jwtResponseUser = jwtResponseApiResponseData.getData();
        Long jwtTeacherId = jwtResponseUser.getId();
        Course course = courseService.findById(courseId);

        if(!course.getTeacher().getUserId().equals(jwtTeacherId)){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("You are not teacher of this course");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }

        lesson.setCourse(course);

        apiResponseData.setData(lessonRepository.save(lesson));
        apiResponseData.setMessage("created lesson");
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<Lesson> updateLesson(Lesson lesson,Long courseId, Authentication authentication) {
        ApiResponseData<Lesson> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if(!jwtResponseApiResponseData.getSuccess()){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("Cannot update lesson");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        Course course = courseService.findById(courseId);
        Lesson lesson1 = lessonById(lesson.getLessonId());
        lesson1.setCourse(course);
        JWTResponse jwtResponseUser = jwtResponseApiResponseData.getData();
        Long jwtTeacherId = jwtResponseUser.getId();
        if(!course.getTeacher().getUserId().equals(jwtTeacherId)){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("You are not teacher of this course");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        lesson1.setUpdatedAt(LocalDateTime.now());
        lesson1.setContentUrl(lesson.getContentUrl());
        lesson1.setTitle(lesson.getTitle());
        lesson1.setIsPublished(lesson.getIsPublished());
        lesson1.setOrderIndex(lesson.getOrderIndex());
        lesson1.setTextContent(lesson.getTextContent());
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setErrors(null);
        apiResponseData.setData(lessonRepository.save(lesson1));
        apiResponseData.setMessage("saved lesson");
        return apiResponseData;
    }

    public ApiResponseData<Lesson> updateStatusLesson(Lesson lesson,Long courseId, Authentication authentication) {
        ApiResponseData<Lesson> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if(!jwtResponseApiResponseData.getSuccess()){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("Cannot update status lesson");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        Course course = courseService.findById(courseId);
        Lesson lesson1 = lessonById(lesson.getLessonId());
        lesson1.setCourse(course);
        JWTResponse jwtResponseUser = jwtResponseApiResponseData.getData();
        Long jwtTeacherId = jwtResponseUser.getId();
        if(!course.getTeacher().getUserId().equals(jwtTeacherId)){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("You are not teacher of this course");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return apiResponseData;
        }
        lesson1.setUpdatedAt(LocalDateTime.now());
        lesson1.setIsPublished(true);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setErrors(null);
        apiResponseData.setData(lessonRepository.save(lesson1));
        apiResponseData.setMessage("saved lesson");
        return apiResponseData;
    }

    public void deleteLessonById(Long lessonId,Long courseId, Authentication authentication) {
        ApiResponseData<Lesson> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if(!jwtResponseApiResponseData.getSuccess()){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("Cannot delete lesson");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        Course course = courseService.findById(courseId);
        JWTResponse jwtResponseUser = jwtResponseApiResponseData.getData();
        Long jwtTeacherId = jwtResponseUser.getId();
        if(!course.getTeacher().getUserId().equals(jwtTeacherId)){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("You are not teacher of this course");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        lessonRepository.deleteById(lessonId);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setErrors(null);
        apiResponseData.setMessage("deleted lesson");
    }
}
