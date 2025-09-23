package ra.edu.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.dto.response.LessonPreviewResponse;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;
import ra.edu.model.entity.Review;
import ra.edu.model.entity.User;
import ra.edu.repo.ReviewRepository;
import ra.edu.service.*;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private LessonService lessonService;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review with id " + id + " not found!"));
    }

    public ApiResponseData<Page<Review>> findAllByCourseId(int page, int size, Long courseId) {
        Page<Review> pageReviews = reviewRepository.findAllByCourse_CourseId(PageRequest.of(page,size),courseId);
        ApiResponseData<Page<Review>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(pageReviews);
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setMessage("Success");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<Review> comment(Review review, Long courseId) {
        Course course = courseService.findById(courseId);
        review.setCourse(course);
        Review savedReview = reviewRepository.save(review);
        ApiResponseData<Review> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(savedReview);
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setMessage("Success");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<Review> update(Review review, Authentication authentication, Long courseId) {
        Course course = courseService.findById(courseId);
        review.setCourse(course);
        ApiResponseData<Review> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseData = authService.getUser(authentication);
        if(!jwtResponseData.getSuccess()){
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("Unauthorized");
            apiResponseData.setErrors(jwtResponseData.getErrors());
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }
        JWTResponse jwtResponse = jwtResponseData.getData();
        Long userId = jwtResponse.getId();
        User studentIdReview = userService.findByUserId(review.getStudent().getUserId());
        if(!userId.equals(studentIdReview.getUserId())) {
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("You are not the owner");
            apiResponseData.setErrors(jwtResponseData.getErrors());
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }
        Review reviewSaved = findById(review.getReviewId());
        reviewSaved.setComment(review.getComment());
        reviewSaved.setRating(review.getRating());
        reviewSaved.setStudent(studentIdReview);
        reviewSaved.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(reviewSaved);

        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setMessage("Success");
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(reviewSaved);
        return apiResponseData;
    }

    public ApiResponseData<Review> delete(Long reviewId, Authentication authentication) {
        Review review =  findById(reviewId);
        ApiResponseData<Review> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseData = authService.getUser(authentication);
        if(!jwtResponseData.getSuccess()){
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("Unauthorized");
            apiResponseData.setErrors(jwtResponseData.getErrors());
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }
        JWTResponse jwtResponse = jwtResponseData.getData();
        Long userId = jwtResponse.getId();
        User studentIdReview = userService.findByUserId(review.getStudent().getUserId());
        if(!userId.equals(studentIdReview.getUserId())) {
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("You are not the owner");
            apiResponseData.setErrors(jwtResponseData.getErrors());
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }
        Review reviewDeleted = findById(review.getReviewId());
        reviewRepository.delete(reviewDeleted);
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setMessage("Success");
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(reviewDeleted);
        return apiResponseData;
    }

    public ApiResponseData<LessonPreviewResponse> getLesson(Long lessonId) {
        Lesson lesson = lessonService.lessonById(lessonId);
        String preview = lesson.getTextContent();
        if (preview != null && preview.length() > 200) {
            preview = preview.substring(0, 200) + "...";
        }

        LessonPreviewResponse dto = new LessonPreviewResponse(
                lesson.getLessonId(),
                lesson.getTitle(),
                preview
        );
        ApiResponseData<LessonPreviewResponse> apiResponseData = new ApiResponseData<>();
        apiResponseData.setSuccess(true);
        apiResponseData.setData(dto);
        apiResponseData.setMessage("Lesson Preview Successfully");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }
}
