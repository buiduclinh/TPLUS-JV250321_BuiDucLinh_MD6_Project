package ra.edu.service;

import org.springframework.security.core.Authentication;
import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.dto.response.TokenVerifyRequest;

public interface AuthService {
    ApiResponseData<JWTResponse> login(UserLogin userLogin);
    ApiResponseData<String> verifyToken(TokenVerifyRequest request);
    ApiResponseData<JWTResponse> getUser();
}
