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
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponseData<User>> register(@RequestBody UserRegister userRegister) {
        ApiResponseData<User> apiResponseData = userService.register(userRegister);
        return ResponseEntity.created(URI.create("/auth/register")).body(apiResponseData);
    }

    @GetMapping("/{status}")
    public ResponseEntity<ApiResponseData<Page<User>>> getUserList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam String role, @PathVariable Boolean status) {
        ApiResponseData<Page<User>> apiResponseData = userService.getUsers(page, size, role, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<ApiResponseData<User>> getUser(@PathVariable Long user_id) {
        ApiResponseData<User> apiResponseData = userService.findById(user_id);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{user_id}/role")
    public ResponseEntity<ApiResponseData<User>> updateRole(@PathVariable Long user_id, @RequestParam List<String> newRole) {
        ApiResponseData<User> apiResponseData = userService.updateRole(user_id, newRole);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{user_id}/status")
    public ResponseEntity<ApiResponseData<User>> updateStatus(@PathVariable Long user_id, @RequestParam Boolean status) {
        ApiResponseData<User> apiResponseData = userService.updateStatus(user_id, status);
        return ResponseEntity.ok(apiResponseData);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponseData<Void>> deleteUser(@PathVariable Long user_id) {
        userService.delete(user_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<ApiResponseData<User>> updateUser(@PathVariable Long user_id, @RequestBody User newUser,@RequestHeader Authentication authentication) {
        ApiResponseData<User> apiResponseData = userService.updateUser(user_id, newUser, authentication);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{user_id}/password")
    public ResponseEntity<ApiResponseData<User>> updatePassword(@PathVariable Long user_id, @RequestBody User newUser,@RequestHeader Authentication authentication) {
        ApiResponseData<User> apiResponseData = userService.updateUserPassword(user_id, newUser, authentication);
        return ResponseEntity.ok(apiResponseData);
    }
}
