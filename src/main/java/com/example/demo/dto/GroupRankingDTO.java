package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupRankingDTO {
    private int rank;
    private String studyGroupName;
    private Long totalTime;
    private Long members;
    private int limit;
    private String leaderName;

    public GroupRankingDTO(int rank, String studyGroupName, Long totalTime, Long members, int limit, String leaderName) {
        this.rank = rank;
        this.studyGroupName = studyGroupName;
        this.totalTime = totalTime;
        this.members = members;
        this.limit = limit;
        this.leaderName = leaderName;
    }

}
