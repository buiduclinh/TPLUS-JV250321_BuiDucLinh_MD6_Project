package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentLesson;
import ra.edu.model.entity.Lesson;
import ra.edu.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<Enrollment>>> enrollmentPage(int page, int size) {
        ApiResponseData<Page<Enrollment>> apiResponseData = enrollmentService.enrollmentPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<Enrollment>> enrollment(@RequestBody Enrollment enrollment, @RequestParam Long courseId, @RequestParam Long studentId) {
        ApiResponseData<Enrollment> apiResponseData = enrollmentService.createEnrollment(enrollment, courseId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @GetMapping("/{enrollment_id}")
    public ResponseEntity<ApiResponseData<Enrollment>> getEnrollmentById(@PathVariable("enrollment_id") Long enrollmentId, @RequestParam Long studentID, @RequestHeader Authentication authentication) {
        ApiResponseData<Enrollment> apiResponseData = enrollmentService.getEnrollmentById(enrollmentId, studentID, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{enrollment_id}/complete_lesson/{lesson_id}")
    public ResponseEntity<ApiResponseData<EnrollmentLesson>> updateCompetedLesson(@PathVariable Enrollment enrollment_id, @PathVariable Lesson lesson_id, @RequestBody EnrollmentLesson enrollmentLesson, @RequestParam long courseId, @RequestParam Long lessonId, @RequestHeader Authentication authentication) {
        enrollmentLesson.setEnrollment(enrollment_id);
        enrollmentLesson.setLesson(lesson_id);
        ApiResponseData<EnrollmentLesson> apiResponseData = enrollmentService.updateLessonCompleted(enrollmentLesson,courseId,lessonId,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

}
