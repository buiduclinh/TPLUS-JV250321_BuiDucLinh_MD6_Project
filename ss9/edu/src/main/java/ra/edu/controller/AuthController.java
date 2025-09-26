package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponseData<JWTResponse>> login(@Valid @RequestBody UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = authService.login(userLogin);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseData<String>> getToken(@Valid @RequestBody TokenVerifyRequest request) {
        return ResponseEntity.ok(authService.verifyToken(request));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseData<JWTResponse>> getUser() {
        return ResponseEntity.ok(authService.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseData<String>> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.logout(token.replace("Bearer ", "")));
    }

}
