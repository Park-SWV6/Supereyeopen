package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter @Setter
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom; // 연결된 채팅방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false) // 보낸 사람
    private UserEntity sender; // senderId 대신 UserEntity 사용

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = true) // 필요시 수신자 설정
    private UserEntity recipient;

    @Column(nullable = false)
    private String content; // 메시지 내용

    @Column(nullable = false)
    private String timestamp;

    @Column(nullable = false)
    private boolean  isRead = false;
}
