package ra.edu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.dto.response.TokenVerifyRequest;
import ra.edu.security.custom.CustomUserDetails;
import ra.edu.security.jwt.JWTProvider;
import ra.edu.service.AuthService;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private AuthenticationProvider authenticationProvider;


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
                .id(customUserDetails.getId())
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

    public ApiResponseData<String> verifyToken(TokenVerifyRequest request) {
        ApiResponseData<String> response = new ApiResponseData<>();
        try {
            boolean isValid = jwtProvider.validateToken(request.getToken());
            if (isValid) {
                response.setSuccess(true);
                response.setMessage("Token is valid");
                response.setStatus(HttpStatus.OK);
                response.setData("VALID");
            } else {
                response.setSuccess(false);
                response.setMessage("Token is invalid or expired");
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setData("INVALID");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error verifying token: " + e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setData("ERROR");
        }
        return response;
    }

    public ApiResponseData<JWTResponse> getUser() {
        ApiResponseData<JWTResponse> apiResponseData = new ApiResponseData<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setErrors(null);
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("User is not authenticated");
            return apiResponseData;
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtProvider.generateToken(customUserDetails.getUsername());
        JWTResponse jwtResponse = JWTResponse.builder()
                .id(customUserDetails.getId())
                .username(customUserDetails.getUsername())
                .email(customUserDetails.getEmail())
                .isActive(customUserDetails.getIsActive())
                .authorities(customUserDetails.getAuthorities())
                .typeAuthentication("Bearer")
                .token(token)
                .build();
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(jwtResponse);
        apiResponseData.setMessage("User Successfully");
        apiResponseData.setStatus(HttpStatus.OK);
        return  apiResponseData;
    }


}
