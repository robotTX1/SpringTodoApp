package com.robottx.springtodoapp.application.user.controller;

import com.robottx.springtodoapp.application.image.service.ProfileImageService;
import com.robottx.springtodoapp.model.user.ProfileImage;
import com.robottx.springtodoapp.model.user.ProfileImageVO;
import com.robottx.springtodoapp.model.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final ProfileImageService profileImageService;

    @Transactional
    @GetMapping("/image")
    public ResponseEntity<?> getProfileImage(User user) {
        ProfileImageVO image = profileImageService.getImageByName(user.getProfileImage().getName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(image.type()))
                .body(image.imageData());
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Void> uploadProfileImage(User user, @RequestParam("image") MultipartFile imageFile) {
        try {
            ProfileImage profileImage = profileImageService.uploadImage(user, imageFile);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/images/{imageId}")
                    .buildAndExpand(profileImage.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
