package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.request.UserRegister;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.entity.User;
import ra.edu.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseData<User>> register(@RequestBody UserRegister userRegister) {
        ApiResponseData<User> apiResponseData = userService.register(userRegister);
        return ResponseEntity.created(URI.create("/auth/register")).body(apiResponseData);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponseData<JWTResponse>> login(@RequestBody UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = userService.login(userLogin);
        return ResponseEntity.ok(apiResponseData);
    }
}
