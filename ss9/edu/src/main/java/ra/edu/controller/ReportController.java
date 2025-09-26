package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.CourseStatisticByStudents;
import ra.edu.model.dto.response.CourseStatisticByTeacher;
import ra.edu.model.dto.response.LessonEnrollmentCourseByStudent;
import ra.edu.service.EnrollmentService;
import ra.edu.service.NotificationService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/top_courses")
    public ResponseEntity<ApiResponseData<Page<CourseStatisticByStudents>>> getTopCourses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        ApiResponseData<Page<CourseStatisticByStudents>> apiResponseData = enrollmentService.courseStatisticByStudents(page, size);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/student_progress/{student_id}")
    public ResponseEntity<ApiResponseData<Page<LessonEnrollmentCourseByStudent>>> lessonEnrollmentCourseByStudent(@PathVariable Long student_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        ApiResponseData<Page<LessonEnrollmentCourseByStudent>> apiResponseData = enrollmentService.lessonEnrollmentCourseByStudents(student_id, page, size);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/teacher_courses_overview/{teacher_id}")
    public ResponseEntity<ApiResponseData<Page<CourseStatisticByTeacher>>> curseStatisticByTeacher(@PathVariable Long teacher_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        ApiResponseData<Page<CourseStatisticByTeacher>> apiResponseData = enrollmentService.courseStatisticByTeacher(page,size,teacher_id);
        return ResponseEntity.ok(apiResponseData);
    }
}
