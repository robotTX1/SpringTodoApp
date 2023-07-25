package com.robottx.springtodoapp.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, UUID> {

    Optional<ProfileImage> findByName(String name);

}
