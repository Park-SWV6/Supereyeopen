package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class QuizDTO {
    private Long id;
    private String question;
    private String answer;
    private Long userId;
    private String userName;
    private List<Long> likes = new ArrayList<>();; // 좋아요 누른 사용자 ID 목록
    private List<Long> dislikes = new ArrayList<>();; // 싫어요 누른 사용자 ID 목록
    private String date; // ISO 형식으로 반환

}
