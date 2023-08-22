package com.vux.security.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private int projectId;
    private int userId;
}
