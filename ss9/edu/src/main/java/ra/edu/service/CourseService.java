package ra.edu.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;

public interface CourseService {
    ApiResponseData<Page<Course>> coursePages(int page, int pageSize, String status, Authentication authentication);
    Course findById(Long id);
    ApiResponseData<Course> getCourse(Long id);
    ApiResponseData<Course> saveCourse(Course course, Long teacherId);
    ApiResponseData<Course> updateCourse(Course course, Long teacherId);
    ApiResponseData<Course> updateStatus(Course course, String status);
    void deleteCourse(Long id);
    ApiResponseData<Page<Course>> findCourseByName(int page, int size,String courseName);
    ApiResponseData<Page<Course>> findCourseByTeacherId(int page, int size,Long teacherId);
    ApiResponseData<Page<Lesson>> lessonPage(int pageNumber, int pageSize, Long courseId, Boolean isPublished);
}
