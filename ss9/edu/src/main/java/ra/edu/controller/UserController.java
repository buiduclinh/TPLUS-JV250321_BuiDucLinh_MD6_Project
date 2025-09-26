package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.request.UserRegister;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.Passwords;
import ra.edu.model.entity.User;
import ra.edu.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponseData<User>> register(@RequestBody UserRegister userRegister) {
        ApiResponseData<User> apiResponseData = userService.register(userRegister);
        return ResponseEntity.created(URI.create("/auth/register")).body(apiResponseData);
    }

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<User>>> getUserList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String role, @RequestParam(required = false) Boolean status) {
        ApiResponseData<Page<User>> apiResponseData = userService.getUsers(page, size, role, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/{user_id}/info")
    public ResponseEntity<ApiResponseData<User>> getUser(@PathVariable("user_id") Long userId) {
        ApiResponseData<User> apiResponseData = userService.findById(userId);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{user_id}/role")
    public ResponseEntity<ApiResponseData<User>> updateRole(@PathVariable("user_id") Long userId, @RequestParam List<String> newRole) {
        ApiResponseData<User> apiResponseData = userService.updateRole(userId, newRole);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{user_id}/status")
    public ResponseEntity<ApiResponseData<User>> updateStatus(@PathVariable("user_id") Long userId) {
        ApiResponseData<User> apiResponseData = userService.updateStatus(userId);
        return ResponseEntity.ok(apiResponseData);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponseData<Void>> deleteUser(@PathVariable("user_id") Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<ApiResponseData<User>> updateUser(@PathVariable("user_id") Long userId,@Valid @RequestBody UserRegister dto) {
        ApiResponseData<User> apiResponseData = userService.updateUser(userId, dto);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{user_id}/password")
    public ResponseEntity<ApiResponseData<User>> updatePassword(@PathVariable("user_id") Long userId,@Valid @RequestBody Passwords passwords) {
        ApiResponseData<User> apiResponseData = userService.updateUserPassword(userId, passwords);
        return ResponseEntity.ok(apiResponseData);
    }
}
