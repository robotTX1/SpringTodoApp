package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.user.Authority;
import com.robottx.springtodoapp.model.user.AuthorityRepository;
import com.robottx.springtodoapp.model.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public void addAuthorityToUser(User user, String authorityName) {
        Authority authority = authorityRepository.findByAuthority(authorityName).orElse(new Authority(authorityName));
        user.addAuthority(authority);
    }
}
