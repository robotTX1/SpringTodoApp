package com.robottx.springtodoapp.application.image.controller;

import com.robottx.springtodoapp.application.image.service.ProfileImageService;
import com.robottx.springtodoapp.model.user.ProfileImageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ProfileImageService profileImageService;

    @RequestMapping("/{imageId}")
    public ResponseEntity<?> getImageById(@PathVariable("imageId") UUID imageId) {
        ProfileImageVO image = profileImageService.getImageById(imageId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(image.type()))
                .body(image.imageData());
    }


}
