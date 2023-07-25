package com.robottx.springtodoapp.application.user.service;

import com.robottx.springtodoapp.model.user.ProfileImage;
import com.robottx.springtodoapp.model.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileImageService {
    ProfileImage uploadImage(User user, MultipartFile imageFile) throws IOException;

    byte[] getImageByName(String name);
}
