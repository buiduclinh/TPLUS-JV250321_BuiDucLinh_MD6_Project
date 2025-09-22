package ra.edu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.User;
import ra.edu.repo.CourseRepository;
import ra.edu.service.CourseService;
import ra.edu.service.UserService;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserService userService;

    public ApiResponseData<Page<Course>> coursePages(int page, int pageSize, String status) {
        Page<Course> coursePage;
        if (status == null) {
            coursePage = courseRepository.findAll(PageRequest.of(page, pageSize));
        } else {
            coursePage = courseRepository.findAll(PageRequest.of(page, pageSize), status);
        }
        ApiResponseData<Page<Course>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Pages Found");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(coursePage);
        return apiResponseData;
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course Not Found"));
    }

    public ApiResponseData<Course> getCourse(Long id) {
        Course course = findById(id);
        ApiResponseData<Course> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Found");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(course);
        return apiResponseData;
    }

    public ApiResponseData<Course> saveCourse(Course course, Long teacherId) {
        User teacher = userService.findByUserId(teacherId);
        course.setTeacher(teacher);
        courseRepository.save(course);
        ApiResponseData<Course> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Saved");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(course);
        return apiResponseData;
    }

    public ApiResponseData<Course> updateCourse(Course course, Long teacherId) {
        Course course1 = courseRepository.findById(course.getCourseId()).orElseThrow(() -> new RuntimeException("Course Not Found"));
        course.setCourseId(course1.getCourseId());
        course.setTeacher(userService.findByUserId(teacherId));
        course.setUpdatedAt(LocalDateTime.now());
        course.setPrice(course.getPrice());
        course.setDescription(course.getDescription());
        course.setDurationHours(course.getDurationHours());
        course.setStatus(course.getStatus());
        courseRepository.save(course);
        ApiResponseData<Course> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Updated");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(course);
        return apiResponseData;
    }

    public ApiResponseData<Course> updateStatus(Course course, String status) {
        Course course1 = findById(course.getCourseId());
        course1.setStatus(status);
        course1.setUpdatedAt(LocalDateTime.now());
        ApiResponseData<Course> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Updated");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(courseRepository.save(course1));
        return apiResponseData;
    }

    public void deleteCourse(Long id) {
        Course course = findById(id);
        if (course != null) {
            courseRepository.delete(course);
            ApiResponseData<Course> apiResponseData = new ApiResponseData<>();
            apiResponseData.setMessage("Course Deleted");
            apiResponseData.setErrors(null);
            apiResponseData.setSuccess(true);
            apiResponseData.setStatus(HttpStatus.OK);
            apiResponseData.setData(course);
        }
    }

    public ApiResponseData<Page<Course>> findCourseByName(int page, int size, String courseName) {
        Page<Course> coursePage = courseRepository.findCourseByTitleContainingIgnoreCase(courseName, PageRequest.of(page, size));
        ApiResponseData<Page<Course>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Found");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(coursePage);
        return apiResponseData;
    }

    public ApiResponseData<Page<Course>> findCourseByTeacherId(int page, int size, Long teacherId) {
        Page<Course> coursePage = courseRepository.findCourseByTeacher_UserId(teacherId, PageRequest.of(page, size));
        ApiResponseData<Page<Course>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Course Found");
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setData(coursePage);
        return apiResponseData;
    }
}
