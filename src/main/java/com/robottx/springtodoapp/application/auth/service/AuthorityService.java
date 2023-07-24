package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.user.User;

public interface AuthorityService {
    void addAuthorityToUser(User user, String authorityName);
}
