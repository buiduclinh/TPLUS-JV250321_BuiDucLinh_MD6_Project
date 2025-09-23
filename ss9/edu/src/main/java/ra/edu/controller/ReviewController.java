package ra.edu.controller;

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

    @PutMapping("/api/reviews/{review_id}")
    public ResponseEntity<ApiResponseData<Review>> updateReview(@RequestBody Review review, @PathVariable Long review_id, @RequestHeader Authentication authentication, @RequestParam Long course_id){
        review.setReviewId(review_id);
        ApiResponseData<Review> apiResponseData = reviewService.update(review,authentication,course_id);
        return ResponseEntity.ok(apiResponseData);
    }

    @DeleteMapping("/api/reviews/{review_id}")
    public ResponseEntity<ApiResponseData<Review>> deleteReview(@PathVariable Long review_id,@RequestHeader Authentication authentication){
        ApiResponseData<Review> apiResponseData = reviewService.delete(review_id,authentication);
        return ResponseEntity.ok(apiResponseData);
    }
}
