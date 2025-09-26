package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Review;
import ra.edu.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PutMapping("/{review_id}")
    public ResponseEntity<ApiResponseData<Review>> updateReview(@Valid @RequestBody Review review, @PathVariable Long review_id, @RequestParam("courseId") Long courseId){
        review.setReviewId(review_id);
        ApiResponseData<Review> apiResponseData = reviewService.update(review,courseId);
        return ResponseEntity.ok(apiResponseData);
    }

    @DeleteMapping("/{review_id}")
    public ResponseEntity<ApiResponseData<Review>> deleteReview(@PathVariable("review_id") Long reviewId){
        ApiResponseData<Review> apiResponseData = reviewService.delete(reviewId);
        return ResponseEntity.ok(apiResponseData);
    }
}
