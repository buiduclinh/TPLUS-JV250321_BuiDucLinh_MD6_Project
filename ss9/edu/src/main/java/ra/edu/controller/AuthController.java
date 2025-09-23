package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.response.*;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Review;
import ra.edu.service.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseData<JWTResponse>> login(@RequestBody UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = authService.login(userLogin);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseData<JWTResponse>> getToken(@RequestBody UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = authService.getToken(userLogin);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseData<JWTResponse>> getUser(@RequestHeader("Authorization") Authentication authorization) {
        ApiResponseData<JWTResponse> apiResponseData = authService.getUser(authorization);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseData<String>> logout(@RequestParam String token) {
        return ResponseEntity.ok(userService.logout(token));
    }

}
