package com.robottx.springtodoapp.application.image.service;

import com.robottx.springtodoapp.application.exception.NotFoundException;
import com.robottx.springtodoapp.application.image.exception.InvalidImageException;
import com.robottx.springtodoapp.application.util.AppUtils;
import com.robottx.springtodoapp.model.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;

    private static final Set<String> ALLOWED_MEDIA_TYPES = Set.of("image/png", "image/jpg", "image/jpeg");

    @Override
    public ProfileImage uploadImage(User user, MultipartFile imageFile) throws IOException {
        if(imageFile.isEmpty())
            throw new InvalidImageException("Image can't be empty");

        if(!ALLOWED_MEDIA_TYPES.contains(imageFile.getContentType()))
            throw new InvalidImageException("Invalid image format: %s. Allowed formats: %s"
                    .formatted(imageFile.getContentType(), String.join(", ", ALLOWED_MEDIA_TYPES)));

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
    public ProfileImageVO getImageByName(String name) {
        ProfileImage profileImage = profileImageRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Can't find image with Name: " + name));

        return new ProfileImageVO(
                profileImage.getId(),
                profileImage.getName(),
                profileImage.getType(),
                AppUtils.decompressImage(profileImage.getImageData()));
    }

    @Override
    @Transactional
    public ProfileImageVO getImageById(UUID imageId) {
        ProfileImage profileImage = profileImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Can't find image by Id: " + imageId));

        return new ProfileImageVO(
                profileImage.getId(),
                profileImage.getName(),
                profileImage.getType(),
                AppUtils.decompressImage(profileImage.getImageData()));
    }
}
