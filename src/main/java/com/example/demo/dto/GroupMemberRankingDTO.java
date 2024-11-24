package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class GroupMemberRankingDTO {
    private int rank;
    private String userName;
    private String profileImageUri;
    private int studyTime;
    private String studyGroupName;

    public GroupMemberRankingDTO(int rank, String userName,String profileImageUri, int studyTime, String studyGroupName) {
        this.rank = rank;
        this.userName = userName;
        this.profileImageUri = profileImageUri;
        this.studyTime = studyTime;
        this.studyGroupName = studyGroupName;
    }
}
