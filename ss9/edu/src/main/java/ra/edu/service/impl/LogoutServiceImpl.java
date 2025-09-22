package ra.edu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.repo.BlackListTokenRepository;
import ra.edu.service.LogoutService;

@Service
@Slf4j
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    public boolean isTokenBlacklisted(String token) {
        return blackListTokenRepository.existsByToken(token);
    }
}
