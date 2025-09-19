package ra.edu.service;

import ra.edu.model.dto.request.UserLogin;
import ra.edu.model.dto.request.UserRegister;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.JWTResponse;
import ra.edu.model.entity.User;

public interface UserService {
    ApiResponseData<User> register(UserRegister userRegister);
    ApiResponseData<JWTResponse> login(UserLogin userLogin);
}
