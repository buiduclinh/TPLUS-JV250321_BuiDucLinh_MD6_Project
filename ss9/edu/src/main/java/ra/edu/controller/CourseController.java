package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.PostComment;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;
import ra.edu.model.entity.Review;
import ra.edu.service.AuthService;
import ra.edu.service.CourseService;
import ra.edu.service.LessonService;
import ra.edu.service.ReviewService;

import java.net.URI;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<Course>>> getAllCourses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "PUBLISHED") String status) {
        ApiResponseData<Page<Course>> apiResponseData = courseService.coursePages(page, pageSize, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<ApiResponseData<Course>> getCourseById(@PathVariable Long course_id) {
        ApiResponseData<Course> courseApiResponseData = courseService.getCourse(course_id);
        return ResponseEntity.ok(courseApiResponseData);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<Course>> createCourse(@Valid @RequestBody Course course, @RequestParam Long teacherId) {
        ApiResponseData<Course> courseApiResponseData = courseService.saveCourse(course, teacherId);
        return ResponseEntity.created(URI.create("courses")).body(courseApiResponseData);
    }

    @PutMapping("/{course_id}")
    public ResponseEntity<ApiResponseData<Course>> updateCourse(@PathVariable Long course_id,@Valid @RequestBody Course course, @RequestParam Long teacherId) {
        course.setCourseId(course_id);
        ApiResponseData<Course> courseApiResponseData = courseService.updateCourse(course, teacherId);
        return ResponseEntity.ok(courseApiResponseData);
    }

    @PutMapping("/{course_id}/status")
    public ResponseEntity<ApiResponseData<Course>> updateStatus(@PathVariable Long course_id, @RequestParam String status) {
        ApiResponseData<Course> courseApiResponseData = courseService.updateStatus(course_id, status);
        return ResponseEntity.ok(courseApiResponseData);
    }

    @DeleteMapping("/{course_id}")
    public ResponseEntity<ApiResponseData<Void>> deleteCourse(@PathVariable Long course_id) {
        courseService.deleteCourse(course_id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{course_id}/lessons")
    public ResponseEntity<ApiResponseData<Page<Lesson>>> findLessonByCourse(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize, @PathVariable Long course_id, @RequestParam(defaultValue = "true") Boolean isPublished) {
        ApiResponseData<Page<Lesson>> apiResponseData = courseService.lessonPage(pageNumber, pageSize, course_id, isPublished);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PostMapping("/{course_id}/lessons")
    public ResponseEntity<ApiResponseData<Lesson>> save(@Valid @RequestBody Lesson lesson, @PathVariable Long course_id) {
        ApiResponseData<Lesson> apiResponseData = lessonService.createLesson(lesson, course_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseData);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseData<Page<Course>>> findCourseByName(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam String keyword) {
        ApiResponseData<Page<Course>> apiResponseData = courseService.findCourseByName(page, size, keyword);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/teacher/{teacher_id}")
    public ResponseEntity<ApiResponseData<Page<Course>>> findCourseByTeacherId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@PathVariable("teacher_id") Long teacherId) {
        ApiResponseData<Page<Course>> apiResponseData = courseService.findCourseByTeacherId(page, size, teacherId);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/{course_id}/reviews")
    public ResponseEntity<ApiResponseData<Page<Review>>> viewsCommentsCourse (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @PathVariable Long course_id){
        ApiResponseData<Page<Review>> apiResponseData = reviewService.findAllByCourseId(page, size, course_id);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/{course_id}/reviews")
    public ResponseEntity<ApiResponseData<Review>> addReview(@Valid @RequestBody PostComment postComment, @PathVariable Long course_id){
        ApiResponseData<Review> apiResponseData = reviewService.comment(postComment,course_id);
        return ResponseEntity.ok(apiResponseData);
    }
}
