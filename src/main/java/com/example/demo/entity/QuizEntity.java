package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quiz_table")
@Getter @Setter
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "quiz_likes",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> likes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "quiz_dislikes",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> dislikes = new ArrayList<>();

    @Column(nullable = false)
    private String date;

    // 계산된 필드 추가
    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }

    public int getDislikeCount() {
        return dislikes != null ? dislikes.size() : 0;
    }

    public int getLikesMinusDislikes() {
        return getLikeCount() - getDislikeCount();
    }
}