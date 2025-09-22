package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.request.UserRegister;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.User;
import ra.edu.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseData<User>> register(@RequestBody UserRegister userRegister) {
        ApiResponseData<User> apiResponseData = userService.register(userRegister);
        return ResponseEntity.created(URI.create("/auth/register")).body(apiResponseData);
    }

    @GetMapping("/userList")
    public ResponseEntity<ApiResponseData<Page<User>>> getUserList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam String role, @RequestParam Boolean status) {
        ApiResponseData<Page<User>> apiResponseData = userService.getUsers(page, size, role, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseData<User>> getUser(@PathVariable Long id) {
        ApiResponseData<User> apiResponseData = userService.findById(id);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponseData<User>> updateRole(@PathVariable Long id, @RequestParam List<String> newRole) {
        ApiResponseData<User> apiResponseData = userService.updateRole(id, newRole);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseData<User>> updateStatus(@PathVariable Long id, @RequestParam Boolean status) {
        ApiResponseData<User> apiResponseData = userService.updateStatus(id, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseData<User>> updateUser(@PathVariable Long id, @RequestBody User newUser,@RequestHeader Authentication authentication) {
        ApiResponseData<User> apiResponseData = userService.updateUser(id, newUser, authentication);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponseData<User>> updatePassword(@PathVariable Long id, @RequestBody User newUser,@RequestHeader Authentication authentication) {
        ApiResponseData<User> apiResponseData = userService.updateUserPassword(id, newUser, authentication);
        return ResponseEntity.ok(apiResponseData);
    }
}
