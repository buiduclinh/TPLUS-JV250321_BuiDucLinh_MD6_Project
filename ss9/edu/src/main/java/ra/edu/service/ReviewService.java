package ra.edu.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.LessonPreviewResponse;
import ra.edu.model.entity.Review;

public interface ReviewService {
    ApiResponseData<Page<Review>> findAllByCourseId(int page, int size, Long courseId);
    ApiResponseData<Review> comment(Review review, Long courseId);
    ApiResponseData<Review> update(Review review, Authentication authentication, Long courseId);
    ApiResponseData<Review> delete(Long reviewId, Authentication authentication);
    ApiResponseData<LessonPreviewResponse> getLesson(Long lessonId);
}
