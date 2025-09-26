package ra.edu.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.request.UserRegister;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.dto.response.Passwords;
import ra.edu.model.entity.Role;
import ra.edu.model.entity.User;

import java.util.List;

public interface UserService {
    ApiResponseData<User> register(UserRegister userRegister);
    List<Role> getRoles(List<String> roles);
    ApiResponseData<Page<User>> getUsers(int page, int size, String role, Boolean status);
    ApiResponseData<User> findById(Long id);
    User findByUserId(Long id);
    ApiResponseData<User> updateRole(Long id, List<String> newRole);
    ApiResponseData<User> updateStatus(Long id);
    void delete(Long id);
    ApiResponseData<User> updateUser(Long id, UserRegister dto);
    ApiResponseData<User> updateUserPassword(Long id, Passwords passwords);
    ApiResponseData<String> logout(String token);
}
