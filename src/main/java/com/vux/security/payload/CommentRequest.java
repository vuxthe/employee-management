package com.vux.security.payload;

import lombok.Getter;

@Getter
public class CommentRequest {
    private int userId;
    private int projectId;
    private String content;
}
