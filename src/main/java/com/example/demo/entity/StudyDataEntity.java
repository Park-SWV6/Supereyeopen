package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name="study_data_table")
public class StudyDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 학습 기록 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 사용자 ID (UserEntity와 연관 가능)

    @Column(nullable = false)
    private LocalDate date; // 학습한 날짜

    @Column(nullable = false)
    private int studyTime; // 학습 시간 (초 단위)
}