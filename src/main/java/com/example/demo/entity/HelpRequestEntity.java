package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter @Getter
@Table(name = "help_request_table")
public class HelpRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @Column(nullable = false)
    private int comments;

    @ElementCollection // 배열이나 리스트를 데이터베이스에 저장하기 위한 어노테이션
    private List<String> uri; // 이미지 경로를 배열로 처리

}
