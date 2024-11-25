package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class QuizDTO {
    private Long id;
    private String question;
    private String answer;
    private Long userId;
    private String userName;
    private List<Long> likes;
    private List<Long> dislikes;
    private String date; // ISO 형식으로 반환
}
