package com.orientdb.backend.post;

import com.orientechnologies.orient.core.id.ORID;

import lombok.Data;

@Data
public class LikeRequest {

    private String postId;

    private String username;

    private String password;
}
