package com.robottx.springtodoapp.model.user;

import java.util.UUID;

public record ProfileImageVO(UUID id, String name, String type, byte[] imageData) {
}
