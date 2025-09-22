package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Course;
import ra.edu.service.CourseService;

import java.net.URI;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<Course>>> getAllCourses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "PUBLISHED") String status){
        ApiResponseData<Page<Course>> apiResponseData = courseService.coursePages(page, pageSize, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseData<Course>> getCourseById(@PathVariable Long id){
        ApiResponseData<Course> courseApiResponseData = courseService.getCourse(id);
        return ResponseEntity.ok(courseApiResponseData);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<Course>> createCourse(@RequestBody Course course, @RequestParam Long teacherId){
        ApiResponseData<Course> courseApiResponseData = courseService.saveCourse(course, teacherId);
        return ResponseEntity.created(URI.create("courses")).body(courseApiResponseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseData<Course>> updateCourse(@PathVariable Long id, @RequestBody Course course, @RequestParam Long teacherId){
        course.setCourseId(id);
        ApiResponseData<Course> courseApiResponseData = courseService.updateCourse(course, teacherId);
        return ResponseEntity.ok(courseApiResponseData);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseData<Course>> updateStatus(@PathVariable Long id,@RequestBody Course course, @RequestParam String status){
        course.setCourseId(id);
        ApiResponseData<Course> courseApiResponseData = courseService.updateStatus(course, status);
        return ResponseEntity.ok(courseApiResponseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
