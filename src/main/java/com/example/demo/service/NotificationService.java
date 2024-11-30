package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.entity.NotificationEntity;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // 알림 생성
    public String createNotification(
            Long receiverId,
            String senderName,
            String title,
            String message,
            String type,
            Long senderId,
            String receivedAt
    ) {
        NotificationEntity notification = new NotificationEntity();
        notification.setReceiverId(receiverId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setType(type);
        notification.setSenderId(senderId);
        notification.setReceivedAt(receivedAt); // 알림 수신 날짜 설정
        // 알림 타입에 따라 name 설정
        notification.setSenderName(resolveNotificationName(type, senderName));

        // 멘토 요청인 경우 isAccepted 초기화
        if ("MENTOR_REQUEST".equals(type)) {
            notification.setIsAccepted(false); // 기본값: 요청 미수락
        }
        notificationRepository.save(notification);
        return "알림이 성공적으로 생성되었습니다.";
    }

    // 사용자별 알림 조회
    public List<NotificationEntity>  getNotificationsByUserId(Long userId) {
        return notificationRepository.findByReceiverId(userId);
    }


    // 읽음 처리
    public void updateReadStatus(Long notificationId, boolean isRead) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));
        notification.setIsRead(isRead);
        notificationRepository.save(notification);
    }

    // 알림 이름 설정
    private String resolveNotificationName(String type, String senderName) {
        return switch (type.toUpperCase()) {
            case "WELCOME", "MENTOR_REQUEST", "MENTOR_REQUEST_ACCEPTED" -> "System"; // 시스템 알림
            case "MESSAGE" -> senderName; // 쪽지 알림
            default -> "Unknown"; // 기본값
        };
    }

    // 알림 수락 상태 업데이트
    public void updateAcceptedStatus(Long notificationId, boolean isAccepted) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        notification.setIsAccepted(isAccepted);
        notificationRepository.save(notification);
    }

}
