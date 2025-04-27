package com.orientdb.backend.post;

import com.orientechnologies.orient.core.id.ORID;

import lombok.Data;

@Data
public class PostReply {

    private String id;

    private String title;

    private String description;

    private String name;

    private String email;

    private Integer likes;
    
}
