package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.LessonPreviewResponse;
import ra.edu.model.entity.Lesson;
import ra.edu.service.LessonService;
import ra.edu.service.ReviewService;


@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{lesson_id}")
    public ResponseEntity<ApiResponseData<Lesson>> findById(@PathVariable("lesson_id") Long lessonId, @RequestParam(defaultValue = "PUBLISHED") String courseStatus) {
        ApiResponseData<Lesson> apiResponseData = lessonService.findById(lessonId, courseStatus);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{lesson_id}")
    public ResponseEntity<ApiResponseData<Lesson>> update(@PathVariable("lesson_id") Long lessonId, @Valid @RequestBody Lesson lesson, @RequestParam Long courseId) {
        lesson.setLessonId(lessonId);
        ApiResponseData<Lesson> apiResponseData = lessonService.updateLesson(lesson,courseId);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{lesson_id}/publish")
    public ResponseEntity<ApiResponseData<Lesson>> updateStatus(@PathVariable("lesson_id") Long lessonId,@Valid @RequestBody Lesson lesson, @RequestParam Long courseId) {
        lesson.setLessonId(lessonId);
        ApiResponseData<Lesson> apiResponseData = lessonService.updateStatusLesson(lesson,courseId);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @DeleteMapping("/{lesson_id}")
    public ResponseEntity<ApiResponseData<Void>> delete(@PathVariable("lesson_id") Long lessonId, @RequestParam Long courseId) {
        lessonService.deleteLessonById(lessonId, courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lesson_id}/content_preview")
    public ResponseEntity<ApiResponseData<LessonPreviewResponse>> previewContent(@PathVariable("lesson_id") Long lessonId){
        ApiResponseData<LessonPreviewResponse> apiResponseData = reviewService.getLesson(lessonId);
        return ResponseEntity.ok(apiResponseData);
    }
}
