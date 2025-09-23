package ra.edu.service;


import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;

public interface LessonService {

    Lesson lessonById(Long lessonId);

    ApiResponseData<Lesson> findById(Long lessonId, String courseStatus);

    ApiResponseData<Lesson> createLesson(Lesson lesson, Long courseId, Authentication authentication);

    ApiResponseData<Lesson> updateLesson(Lesson lesson, Long courseId, Authentication authentication);

    ApiResponseData<Lesson> updateStatusLesson(Lesson lesson, Long courseId, Authentication authentication);

    void deleteLessonById(Long lessonId,Long courseId, Authentication authentication);
}
