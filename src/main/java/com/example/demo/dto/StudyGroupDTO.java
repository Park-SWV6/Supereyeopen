package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class StudyGroupDTO {
    private Long id;
    private String name;
    private String description;
    private int limit;
    private int membersCount;
    private String imageUri;
    private Long leaderId;
    private String leaderName;

    public StudyGroupDTO(Long id, String name, String description, int limit, int membersCount, String imageUri, Long leaderId, String leaderName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.limit = limit;
        this.membersCount = membersCount;
        this.imageUri = imageUri;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
    }
    public StudyGroupDTO() {
        // 빈 생성자
    }

}
