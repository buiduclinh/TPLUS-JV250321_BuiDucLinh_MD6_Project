package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Lesson;
import ra.edu.service.LessonService;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<Lesson>>> findAll(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(defaultValue = "PUBLISHED") String courseStatus) {
        ApiResponseData<Page<Lesson>> apiResponseData = lessonService.lessonPage(pageNumber, pageSize, courseStatus);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseData<Lesson>> findById(@PathVariable Long id, @RequestParam(defaultValue = "PUBLISHED") String courseStatus) {
        ApiResponseData<Lesson> apiResponseData = lessonService.findById(id, courseStatus);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<Lesson>> save(@RequestBody Lesson lesson, @RequestParam Long courseId, @RequestHeader Authentication authentication) {
        ApiResponseData<Lesson> apiResponseData = lessonService.createLesson(lesson, courseId, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseData<Lesson>> update(@PathVariable Long id, @RequestBody Lesson lesson,@RequestParam Long courseId, @RequestHeader Authentication authentication) {
        lesson.setLessonId(id);
        ApiResponseData<Lesson> apiResponseData = lessonService.updateLesson(lesson,courseId,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseData<Lesson>> updateStatus(@PathVariable Long id,@RequestBody Lesson lesson, @RequestParam Long courseId, @RequestHeader Authentication authentication) {
        lesson.setLessonId(id);
        ApiResponseData<Lesson> apiResponseData = lessonService.updateStatusLesson(lesson,courseId,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> delete(@PathVariable Long id, @RequestParam Long courseId, @RequestHeader Authentication authentication) {
        lessonService.deleteLessonById(id, courseId, authentication);
        return ResponseEntity.ok().build();
    }
}
