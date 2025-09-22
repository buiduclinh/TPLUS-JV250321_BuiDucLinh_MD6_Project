package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.entity.Course;
import ra.edu.service.AuthService;
import ra.edu.service.CourseService;
import ra.edu.service.UserService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @GetMapping("/api/courses/status/{status}")
    public ResponseEntity<ApiResponseData<Page<Course>>> getAllCourses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize, @PathVariable String status){
        ApiResponseData<Page<Course>> apiResponseData = courseService.coursePages(page, pageSize, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/api/courses/status/")
    public ResponseEntity<ApiResponseData<Page<Course>>> getAllCoursesAdmin(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize, @RequestParam String status){
        ApiResponseData<Page<Course>> apiResponseData = courseService.coursePages(page, pageSize, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseData<JWTResponse>> login(@RequestBody UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = authService.login(userLogin);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/getToken")
    public ResponseEntity<ApiResponseData<JWTResponse>> getToken(@RequestBody UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = authService.getToken(userLogin);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/getUser")
    public ResponseEntity<ApiResponseData<JWTResponse>> getUser(@RequestHeader("Authorization") Authentication authorization) {
        ApiResponseData<JWTResponse> apiResponseData = authService.getUser(authorization);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/findCourseByName")
    public ResponseEntity<ApiResponseData<Page<Course>>> findCourseByName(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam String courseName) {
        ApiResponseData<Page<Course>> apiResponseData = courseService.findCourseByName(page, size, courseName);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/findCourseByTeacherId")
    public ResponseEntity<ApiResponseData<Page<Course>>> findCourseByTeacherId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@RequestParam Long teacherId) {
        ApiResponseData<Page<Course>> apiResponseData = courseService.findCourseByTeacherId(page, size, teacherId);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseData<String>> logout(@RequestParam String token) {
        return ResponseEntity.ok(userService.logout(token));
    }
}
