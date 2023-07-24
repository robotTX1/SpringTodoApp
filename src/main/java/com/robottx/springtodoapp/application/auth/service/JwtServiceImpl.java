package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.application.auth.exception.AuthException;
import com.robottx.springtodoapp.model.user.Authority;
import com.robottx.springtodoapp.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${security.jwt.expiryTime}")
    private long jwtExpiryTime;

    @Override
    public String generateToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpiryTime))
                .claim("authorities", encodeAuthorities(user))
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(jwtClaimsSet);

        return jwtEncoder.encode(parameters).getTokenValue();
    }

    @Override
    public boolean validateToken(String accessToken) {
        try {
            jwtDecoder.decode(accessToken);
            return true;
        } catch (JwtException ex) {
            if(ex.getMessage().contains("expired")) throw new AuthException("Access Token is expired");
            else if(ex.getMessage().contains("Malformed") || ex.getMessage().contains("delimiter"))
                throw new AuthException("Access Token is malformed");

            log.info(ex.getMessage());
        }

        return false;
    }

    @Override
    public Authentication getAuthentication(String accessToken) {
        Jwt jwt = jwtDecoder.decode(accessToken);

        return new JwtAuthenticationToken(jwt, decodeAuthorities(jwt));
    }

    private String encodeAuthorities(User user) {
        return String.join(",", user.getAuthorities()
                .stream()
                .map(Authority::getAuthority)
                .toList());
    }

    private Collection<? extends GrantedAuthority> decodeAuthorities(Jwt jwt) {
        String authorities = jwt.getClaim("authorities");

        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
