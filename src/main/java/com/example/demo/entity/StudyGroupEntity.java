package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Getter @Setter
@Table(name = "study_groups")
public class StudyGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = true)
    @JsonBackReference
    private UserEntity leader;

    @Column(nullable = true)
    private String description;

    @Column(name = "members_limit", nullable = false)
    private int limit;

    @Column(nullable = false)
    private int membersCount = 0;

    @Column(nullable = true)
    private String imageUri;

    // 스터디 그룹의 멤버들 (OneToMany 관계)
    @OneToMany(mappedBy = "studyGroup")
    @JsonManagedReference
    private List<UserEntity> members = new ArrayList<>();
    // Getters and setters
}
