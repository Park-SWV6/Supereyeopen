package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_table")
@Getter @Setter
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long receiverId; // 수신자 ID

    @Column(nullable = false)
    private Long senderId; // 쪽지를 보낸 사람의 Id(MESSAGE 타입만 사용)

    @Column(nullable = false)
    private String senderName; // 알 이름(SYSTEM OR USERNAME)

    @Column(nullable = false)
    private String title; // 알림 제목

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean IsRead = false;

    @Column(nullable = true) // 멘토 요청이 아닌 알림에선 사용 안함
    private Boolean IsAccepted = false; // 요청 수락 여부(MENTOR_REQUEST 만 해당)

    @Column(nullable = false)
    private String receivedAt;

    @Column(nullable = false)
    private String type; // 알림 타입 ("WELCOME", "MENTOR_REQUEST", "MENTOR_REQUEST_ACCEPTED", "MESSAGE")


}
