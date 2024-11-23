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
    private int studyTime;
    private String studyGroupName;

    public DailyRankingDTO(String userName, int studyTime, String studyGroupName) {
        this.userName = userName;
        this.studyTime = studyTime;
        this.studyGroupName = studyGroupName;
    }
}
