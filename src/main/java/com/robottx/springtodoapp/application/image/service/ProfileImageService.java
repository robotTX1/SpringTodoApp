package com.robottx.springtodoapp.application.image.service;

import com.robottx.springtodoapp.model.user.ProfileImage;
import com.robottx.springtodoapp.model.user.ProfileImageVO;
import com.robottx.springtodoapp.model.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ProfileImageService {
    ProfileImage uploadImage(User user, MultipartFile imageFile) throws IOException;

    ProfileImageVO getImageByName(String name);

    ProfileImageVO getImageById(UUID imageId);
}
