package ra.edu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.request.UserRegister;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.entity.BlackListToken;
import ra.edu.model.entity.Role;
import ra.edu.model.entity.User;
import ra.edu.repo.BlackListTokenRepository;
import ra.edu.repo.RoleRepository;
import ra.edu.repo.UserRepository;
import ra.edu.security.custom.CustomUserDetails;
import ra.edu.security.jwt.JWTProvider;
import ra.edu.service.AuthService;
import ra.edu.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    public ApiResponseData<User> register(UserRegister userRegister) {
        log.info("UserRegister roles = {}", userRegister.getRole());
        User user = User.builder()
                .username(userRegister.getUsername())
                .passwordHash(passwordEncoder.encode(userRegister.getPassword()))
                .email(userRegister.getEmail())
                .isActive(true)
                .role(getRoles(userRegister.getRole()))
                .build();
        userRepository.save(user);
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        apiResponseData.setMessage("Created User Successfully");
        apiResponseData.setStatus(HttpStatus.CREATED);
        apiResponseData.setErrors(null);
        apiResponseData.setData(userRepository.save(user));
        apiResponseData.setSuccess(true);
        return apiResponseData;
    }

    public List<Role> getRoles(List<String> roles) {
        List<Role> roleList = new ArrayList<>();
        if (roles == null || roles.isEmpty()) {
            Role defaultRole = roleRepository.findByRole("STUDENT")
                    .orElseThrow(() -> new NoSuchElementException("Default Role Not Found"));
            roleList.add(defaultRole);
        } else {
            roles.forEach(role -> {
                Role role1 = roleRepository.findByRole(role)
                        .orElseThrow(() -> new NoSuchElementException("Role Not Found"));
                roleList.add(role1);
            });
        }
        return roleList;
    }

    public ApiResponseData<Page<User>> getUsers(int page, int size, String role, Boolean status) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size), role, status);
        ApiResponseData<Page<User>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(users);
        apiResponseData.setMessage("Users list Successfully");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<User> findById(Long id) {
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            apiResponseData.setSuccess(true);
            apiResponseData.setErrors(null);
            apiResponseData.setData(user.get());
            apiResponseData.setMessage("User Successfully Found");
            apiResponseData.setStatus(HttpStatus.OK);
        } else {
            apiResponseData.setSuccess(false);
            apiResponseData.setErrors(null);
            apiResponseData.setMessage("User Not Found");
            apiResponseData.setStatus(HttpStatus.NOT_FOUND);
        }
        return apiResponseData;
    }

    public User findByUserId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User Not Found"));
    }

    public ApiResponseData<User> updateRole(Long id, List<String> newRole) {
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        User user = findByUserId(id);

        if (user.getRole().contains("ADMIN")) {
            apiResponseData.setSuccess(false);
            apiResponseData.setErrors(null);
            apiResponseData.setMessage("Can't update Admin User");
            apiResponseData.setStatus(HttpStatus.FORBIDDEN);
            return apiResponseData;
        }

        List<Role> roles = getRoles(newRole);
        user.setRole(roles);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        user.setUpdatedAt(LocalDateTime.now());
        apiResponseData.setData(userRepository.save(user));
        apiResponseData.setMessage("User Successfully Updated");
        return  apiResponseData;
    }

    public ApiResponseData<User> updateStatus(Long id, Boolean status) {
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        User user = findByUserId(id);
        if (user.getRole().contains("ADMIN")) {
            apiResponseData.setSuccess(false);
            apiResponseData.setErrors(null);
            apiResponseData.setMessage("Can't update Admin User");
            apiResponseData.setStatus(HttpStatus.FORBIDDEN);
            return apiResponseData;
        }
        user.setIsActive(status);
        userRepository.save(user);
        user.setUpdatedAt(LocalDateTime.now());
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(user);
        apiResponseData.setMessage("User Successfully Updated");
        return  apiResponseData;
    }

    public void delete(Long id) {
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        User user = findByUserId(id);
        if (user.getRole().contains("ADMIN") ) {
            apiResponseData.setSuccess(false);
            apiResponseData.setErrors(null);
            apiResponseData.setMessage("Can't delete Admin User");
            apiResponseData.setStatus(HttpStatus.FORBIDDEN);
            return;
        }
        userRepository.deleteById(id);
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(user);
        apiResponseData.setMessage("User Successfully Deleted");
        apiResponseData.setStatus(HttpStatus.OK);
    }

    public ApiResponseData<User> updateUser(Long id, User newUser,Authentication authentication) {
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if(!jwtResponseApiResponseData.getSuccess()){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("Invalid Token");
            return apiResponseData;
        }
        User user = findByUserId(id);
        JWTResponse jwtResponse = jwtResponseApiResponseData.getData();
        Long userLogin = jwtResponse.getId();
        if(!userLogin.equals(user.getUserId())){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("You cannot update User");
            return apiResponseData;
        }
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setIsActive(user.getIsActive());
        newUser.setRole(user.getRole());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPasswordHash(user.getPasswordHash());
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(user);
        apiResponseData.setMessage("User Successfully Updated");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<User> updateUserPassword(Long id, User newUser,Authentication authentication) {
        ApiResponseData<User> apiResponseData = new ApiResponseData<>();
        ApiResponseData<JWTResponse> jwtResponseApiResponseData = authService.getUser(authentication);
        if(!jwtResponseApiResponseData.getSuccess()){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("Invalid Token");
            return apiResponseData;
        }
        User user = findByUserId(id);
        JWTResponse jwtResponse = jwtResponseApiResponseData.getData();
        Long userLogin = jwtResponse.getId();
        if(!userLogin.equals(user.getUserId())){
            apiResponseData.setErrors(jwtResponseApiResponseData.getErrors());
            apiResponseData.setSuccess(false);
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setMessage("You cannot update User");
            return apiResponseData;
        }
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setPasswordHash(user.getPasswordHash());
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(user);
        apiResponseData.setMessage("User Successfully Updated");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }

    public ApiResponseData<String> logout(String token) {
        if(!jwtProvider.validateToken(token)) {
            return ApiResponseData.<String>builder()
                    .success(false)
                    .message("Token out or Invalid Token")
                    .build();
        }
        Date expiredAt = jwtProvider.getExpirationDateFromToken(token);

        BlackListToken blacklistToken = new BlackListToken();
        blacklistToken.setToken(token);
        blacklistToken.setExpiredAt(expiredAt);
        blackListTokenRepository.save(blacklistToken);

        return ApiResponseData.<String>builder()
                .success(true)
                .message("logout success")
                .data(token)
                .build();
    }

    public ApiResponseData<Page<User>> userPage(int page, int size, @Param("status") Boolean status) {
        Page<User> userPage = userRepository.findAllUserByStatus(status, PageRequest.of(page,size));
        ApiResponseData<Page<User>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setSuccess(true);
        apiResponseData.setData(userPage);
        apiResponseData.setErrors(null);
        apiResponseData.setMessage("User Page Successfully");
        apiResponseData.setStatus(HttpStatus.OK);
        return apiResponseData;
    }


}
