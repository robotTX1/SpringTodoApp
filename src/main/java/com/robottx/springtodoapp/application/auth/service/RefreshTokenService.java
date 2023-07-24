package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.auth.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(UUID token);

    RefreshToken generateToken();

}
