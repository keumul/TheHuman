package by.sapegina.springblog.service;

import by.sapegina.springblog.entity.RefreshToken;
import by.sapegina.springblog.exceptions.TheHumanException;
import by.sapegina.springblog.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }
    void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TheHumanException("Error: Invalid refresh token!"));
    }
    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
