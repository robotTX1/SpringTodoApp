package com.robottx.springtodoapp.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Entity
@Builder
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String type;

    @Lob
    @Column(length = 1024)
    private byte[] imageData;

}
