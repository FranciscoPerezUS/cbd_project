package com.orientdb.backend.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
    
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}