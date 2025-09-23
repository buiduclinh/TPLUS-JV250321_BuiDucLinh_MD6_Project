package ra.edu.controller;

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
    public ResponseEntity<ApiResponseData<Lesson>> findById(@PathVariable Long lesson_id, @RequestParam(defaultValue = "PUBLISHED") String courseStatus) {
        ApiResponseData<Lesson> apiResponseData = lessonService.findById(lesson_id, courseStatus);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{lesson_id}")
    public ResponseEntity<ApiResponseData<Lesson>> update(@PathVariable Long lesson_id, @RequestBody Lesson lesson,@RequestParam Long courseId, @RequestHeader Authentication authentication) {
        lesson.setLessonId(lesson_id);
        ApiResponseData<Lesson> apiResponseData = lessonService.updateLesson(lesson,courseId,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{lesson_id}/publish")
    public ResponseEntity<ApiResponseData<Lesson>> updateStatus(@PathVariable Long lesson_id,@RequestBody Lesson lesson, @RequestParam Long courseId, @RequestHeader Authentication authentication) {
        lesson.setLessonId(lesson_id);
        ApiResponseData<Lesson> apiResponseData = lessonService.updateStatusLesson(lesson,courseId,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @DeleteMapping("/{lesson_id}")
    public ResponseEntity<ApiResponseData<Void>> delete(@PathVariable Long lesson_id, @RequestParam Long courseId, @RequestHeader Authentication authentication) {
        lessonService.deleteLessonById(lesson_id, courseId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lesson_id}/content_preview")
    public ResponseEntity<ApiResponseData<LessonPreviewResponse>> previewContent(@PathVariable Long lesson_id){
        ApiResponseData<LessonPreviewResponse> apiResponseData = reviewService.getLesson(lesson_id);
        return ResponseEntity.ok(apiResponseData);
    }
}
