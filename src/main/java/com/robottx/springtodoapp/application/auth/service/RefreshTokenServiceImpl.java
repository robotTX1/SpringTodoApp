package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.auth.RefreshToken;
import com.robottx.springtodoapp.model.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${security.refresh-token.expiryTime}")
    private long expiryTime;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByToken(UUID token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken generateToken() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (refreshTokenRepository.existsByToken(uuid));


        return RefreshToken
                .builder()
                .token(uuid)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(expiryTime))
                .build();
    }
}
