package com.Token.DTOs;

import java.util.Base64;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private String username;
    private String imageBase64;

    public UserProfileDTO(String username, byte[] imageBytes) {
        this.username = username;
        if (imageBytes != null && imageBytes.length > 0) {
            this.imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    // getters e setters
}

