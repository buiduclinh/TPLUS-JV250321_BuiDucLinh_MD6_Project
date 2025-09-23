package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.CourseStatisticByStudents;
import ra.edu.model.dto.response.CourseStatisticByTeacher;
import ra.edu.model.dto.response.LessonEnrollmentCourseByStudent;
import ra.edu.service.NotificationService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/api/reports/top_courses")
    public ResponseEntity<ApiResponseData<Page<CourseStatisticByStudents>>> getTopCourses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        ApiResponseData<Page<CourseStatisticByStudents>> apiResponseData = notificationService.courseStatisticByStudents(page, size);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/api/reports/student_progress/{student_id}")
    public ResponseEntity<ApiResponseData<Page<LessonEnrollmentCourseByStudent>>> lessonEnrollmentCourseByStudent(@PathVariable Long student_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        ApiResponseData<Page<LessonEnrollmentCourseByStudent>> apiResponseData = notificationService.lessonEnrollmentCourseByStudents(student_id, page, size);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/api/reports/teacher_courses_overview/{teacher_id}")
    public ResponseEntity<ApiResponseData<Page<CourseStatisticByTeacher>>> curseStatisticByTeacher(@PathVariable Long teacher_id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        ApiResponseData<Page<CourseStatisticByTeacher>> apiResponseData = notificationService.courseStatisticByTeacher(page,size,teacher_id);
        return ResponseEntity.ok(apiResponseData);
    }
}
