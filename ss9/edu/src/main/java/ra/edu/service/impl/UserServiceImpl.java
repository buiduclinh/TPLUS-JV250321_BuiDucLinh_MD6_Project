package ra.edu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ra.edu.model.entity.Role;
import ra.edu.model.entity.User;
import ra.edu.repo.RoleRepository;
import ra.edu.repo.UserRepository;
import ra.edu.security.custom.CustomUserDetails;
import ra.edu.security.jwt.JWTProvider;
import ra.edu.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private RoleRepository roleRepository;

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

    private List<Role> getRoles(List<String> roles) {
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

    public ApiResponseData<JWTResponse> login(UserLogin userLogin) {
        Authentication authentication = null;
        ApiResponseData<JWTResponse> apiResponseData = new ApiResponseData<>();
        try {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(),userLogin.getPassword()));
        }catch (Exception e){
            log.error("Username or password is incorrect");
            apiResponseData.setMessage("Username or password is incorrect");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setErrors(null);
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtProvider.generateToken(customUserDetails.getUsername());
        JWTResponse jwtResponse = JWTResponse.builder()
                .username(customUserDetails.getUsername())
                .password(customUserDetails.getPassword())
                .authorities(customUserDetails.getAuthorities())
                .token(token)
                .build();
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(jwtResponse);
        apiResponseData.setMessage("Login Successfully");
        return apiResponseData;
    }
}
