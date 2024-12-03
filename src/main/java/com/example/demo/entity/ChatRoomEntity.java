package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chat_rooms_table")
@Getter @Setter
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long mentorId; // 멘토 사용자 ID

    @Column(nullable = false)
    private Long menteeId; // 멘티 사용자 ID

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = true)
    private String lastMessage;

    @Column(nullable = false)
    private Long unreadMessages =0L;

    @OneToOne
    @JoinColumn(name = "mentor_relationship_id", nullable = true)
    private MentorRelationshipsEntity mentorRelationship;

}
