package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Course;

public interface CourseService {
    ApiResponseData<Page<Course>> coursePages(int page, int pageSize, String status);
    Course findById(Long id);
    ApiResponseData<Course> getCourse(Long id);
    ApiResponseData<Course> saveCourse(Course course, Long teacherId);
    ApiResponseData<Course> updateCourse(Course course, Long teacherId);
    ApiResponseData<Course> updateStatus(Course course, String status);
    void deleteCourse(Long id);
    ApiResponseData<Page<Course>> findCourseByName(int page, int size,String courseName);
    ApiResponseData<Page<Course>> findCourseByTeacherId(int page, int size,Long teacherId);
}
