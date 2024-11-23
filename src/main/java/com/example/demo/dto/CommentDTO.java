package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String userName;
    private String content;
    private String date;
}
