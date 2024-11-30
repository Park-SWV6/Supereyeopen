package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name="mentor_relationships_table")
public class MentorRelationshipsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 멘토 (다대일 관계)
    @JoinColumn(name = "mentor_id", nullable = false)
    private UserEntity mentor;

    @ManyToOne(fetch = FetchType.LAZY) // 멘티 (다대일 관계)
    @JoinColumn(name = "mentee_id", nullable = false)
    private UserEntity mentee;

    @Column(nullable = false)
    private String createdAt;

}
