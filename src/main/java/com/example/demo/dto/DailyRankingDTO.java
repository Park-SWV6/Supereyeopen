package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyRankingDTO {
    private int rank;
    private String userName;
    private Long userId;
    private String profileImageUri;
    private int studyTime;
    private String studyGroupName;

    public DailyRankingDTO(String userName, Long userId, String profileImageUri, int studyTime, String studyGroupName) {
        this.userName = userName;
        this.userId = userId;
        this.profileImageUri = profileImageUri;
        this.studyTime = studyTime;
        this.studyGroupName = studyGroupName;
    }
}
