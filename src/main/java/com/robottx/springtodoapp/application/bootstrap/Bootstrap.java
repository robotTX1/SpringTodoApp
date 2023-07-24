package com.robottx.springtodoapp.application.bootstrap;

import com.robottx.springtodoapp.application.auth.service.AuthorityService;
import com.robottx.springtodoapp.model.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        if(!userRepository.existsByEmail("admin@example.com")) {
            User user = User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("password"))
                    .username("Admin")
                    .build();

            authorityService.addAuthorityToUser(user, "ADMIN_READ");
            authorityService.addAuthorityToUser(user, "ADMIN_WRITE");
            authorityService.addAuthorityToUser(user, "USER_READ");
            authorityService.addAuthorityToUser(user, "USER_WRITE");

            userRepository.save(user);
        }
    }
}
