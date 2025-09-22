package ra.edu.service;

import ra.edu.model.dto.response.ApiResponseData;

public interface LogoutService {
    boolean isTokenBlacklisted(String token);
}
