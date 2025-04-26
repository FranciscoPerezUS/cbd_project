package com.orientdb.backend.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class User {
    private String id; // OrientDB record ID

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
    @NotBlank
    private String name;

    @NotBlank
    private String email;

}
