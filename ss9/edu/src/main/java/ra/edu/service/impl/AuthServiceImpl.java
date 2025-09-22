package ra.edu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.security.custom.CustomUserDetails;
import ra.edu.security.custom.CustomUserDetailsService;
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

    public ApiResponseData<JWTResponse> getToken(UserLogin userLogin) {
        ApiResponseData<JWTResponse> apiResponseData = new ApiResponseData<>();
        try {
            String token = jwtProvider.generateToken(userLogin.getUsername());
            JWTResponse jwtResponse = JWTResponse.builder()
                    .username(userLogin.getUsername())
                    .token(token)
                    .typeAuthentication("Bearer")
                    .build();
            apiResponseData.setSuccess(true);
            apiResponseData.setErrors(null);
            apiResponseData.setData(jwtResponse);
            apiResponseData.setMessage("Token Successfully");
            apiResponseData.setStatus(HttpStatus.OK);
            return  apiResponseData;
        }catch (Exception e){
            log.error("Error in getting token",e.getMessage());
            apiResponseData.setMessage("Error in getting token");
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setErrors(null);
            apiResponseData.setSuccess(false);
            return apiResponseData;
        }
    }

    public ApiResponseData<JWTResponse> getUser(Authentication authentication) {
        ApiResponseData<JWTResponse> apiResponseData = new ApiResponseData<>();
        if(authentication == null || !authentication.isAuthenticated()){
            apiResponseData.setStatus(HttpStatus.UNAUTHORIZED);
            apiResponseData.setErrors(null);
            apiResponseData.setSuccess(false);
            apiResponseData.setMessage("User is not authenticated");
            return apiResponseData;
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        JWTResponse jwtResponse = JWTResponse.builder()
                .username(customUserDetails.getUsername())
                .email(customUserDetails.getEmail())
                .isActive(customUserDetails.getIsActive())
                .authorities(customUserDetails.getAuthorities())
                .typeAuthentication("Bearer")
                .build();
        apiResponseData.setSuccess(true);
        apiResponseData.setErrors(null);
        apiResponseData.setData(jwtResponse);
        apiResponseData.setMessage("User Successfully");
        apiResponseData.setStatus(HttpStatus.OK);
        return  apiResponseData;
    }
}
