package ra.edu.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.dto.response.LessonPreviewResponse;
import ra.edu.model.dto.response.PostComment;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Lesson;
import ra.edu.model.entity.Review;
import ra.edu.model.entity.User;
import ra.edu.repo.ReviewRepository;
import ra.edu.repo.UserRepository;
import ra.edu.service.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    private UserRepository userRepository;

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

    public ApiResponseData<Review> comment(PostComment postComment, Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User>  student = userRepository.findByUsername(authentication.getName());
        Course course = courseService.findById(courseId);

        Review review =  new Review();
        review.setComment(postComment.getComment());
        review.setCourse(course);
        review.setStudent(student.get());
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(postComment.getRating());

        ApiResponseData<Review> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(reviewRepository.save(review));
        apiResponseData.setErrors(null);
        apiResponseData.setSuccess(true);
        apiResponseData.setMessage("Success");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<Review> update(Review review, Long courseId) {
        Review reviewSaved = findById(review.getReviewId());
        Course course = courseService.findById(courseId);
        reviewSaved.setCourse(course);
        ApiResponseData<Review> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseData = authService.getUser();
        if(!jwtResponseData.getSuccess()){
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("Unauthorized");
            apiResponseData.setErrors(jwtResponseData.getErrors());
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }

        JWTResponse jwtResponse = jwtResponseData.getData();
        Long userId = jwtResponse.getId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User studentIdReview = userService.findByUserId(reviewSaved.getStudent().getUserId());
        if(!userId.equals(studentIdReview.getUserId()) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("You are not the owner");
            apiResponseData.setErrors(jwtResponseData.getErrors());
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }

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

    public ApiResponseData<Review> delete(Long reviewId) {
        Review review =  findById(reviewId);
        ApiResponseData<Review> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseData = authService.getUser();
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
