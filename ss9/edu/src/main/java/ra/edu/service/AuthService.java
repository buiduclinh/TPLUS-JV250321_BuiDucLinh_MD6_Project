package ra.edu.service;

import org.springframework.security.core.Authentication;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;

public interface AuthService {
    ApiResponseData<JWTResponse> login(UserLogin userLogin);
    ApiResponseData<JWTResponse> getToken(UserLogin userLogin);
    ApiResponseData<JWTResponse> getUser(Authentication authentication);
}
