package com.robottx.springtodoapp.application.user.service;

import com.robottx.springtodoapp.application.exception.NotFoundException;
import com.robottx.springtodoapp.application.util.AppUtils;
import com.robottx.springtodoapp.model.user.ProfileImage;
import com.robottx.springtodoapp.model.user.ProfileImageRepository;
import com.robottx.springtodoapp.model.user.User;
import com.robottx.springtodoapp.model.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;

    @Override
    public ProfileImage uploadImage(User user, MultipartFile imageFile) throws IOException {
        ProfileImage profileImage = ProfileImage.builder()
                .name(UUID.randomUUID().toString())
                .type(imageFile.getContentType())
                .imageData(AppUtils.compressImage(imageFile.getBytes()))
                .build();

        user.setProfileImage(profileImage);
        User savedUser = userRepository.save(user);
        return savedUser.getProfileImage();
    }

    @Override
    @Transactional
    public byte[] getImageByName(String name) {
        ProfileImage profileImage = profileImageRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Can't find image with Name: " + name));

        byte[] bytes = profileImage.getImageData();

        return AppUtils.decompressImage(bytes);
    }
}
