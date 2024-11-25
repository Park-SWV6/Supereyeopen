package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupRankingDTO {
    private int rank;
    private Long studyGroupId;
    private String studyGroupName;
    private String imageUri;
    private Long totalTime;
    private Long members;
    private int limit;
    private String leaderName;

    public GroupRankingDTO(int rank, Long studyGroupId, String studyGroupName, String imageUri, Long totalTime, Long members, int limit, String leaderName) {
        this.rank = rank;
        this.studyGroupId = studyGroupId;
        this.studyGroupName = studyGroupName;
        this.imageUri = imageUri;
        this.totalTime = totalTime;
        this.members = members;
        this.limit = limit;
        this.leaderName = leaderName;
    }

}
