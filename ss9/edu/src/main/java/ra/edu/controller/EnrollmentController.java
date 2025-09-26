package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.Lesson;
import ra.edu.model.entity.LessonProgress;
import ra.edu.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<Enrollment>>> enrollmentPage(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        ApiResponseData<Page<Enrollment>> apiResponseData = enrollmentService.enrollmentPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<Enrollment>> enrollment(@Valid @RequestBody Enrollment enrollment, @RequestParam Long courseId, @RequestParam Long studentId) {
        ApiResponseData<Enrollment> apiResponseData = enrollmentService.createEnrollment(enrollment, courseId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @GetMapping("/{enrollment_id}")
    public ResponseEntity<ApiResponseData<Enrollment>> getEnrollmentById(@PathVariable("enrollment_id") Long enrollmentId, @RequestParam Long studentID) {
        ApiResponseData<Enrollment> apiResponseData = enrollmentService.getEnrollmentById(enrollmentId, studentID);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{enrollment_id}/complete_lesson/{lesson_id}")
    public ResponseEntity<ApiResponseData<LessonProgress>> updateCompetedLesson(@PathVariable("enrollment_id") Long enrollmentId, @PathVariable("lesson_id") Long lessonId ,@Valid @RequestBody LessonProgress lessonProgress) {
        ApiResponseData<LessonProgress> apiResponseData = enrollmentService.updateLessonCompleted(enrollmentId,lessonId,lessonProgress);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

}
