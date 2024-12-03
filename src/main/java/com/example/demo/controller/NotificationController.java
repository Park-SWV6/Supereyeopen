package com.example.demo.controller;

import com.example.demo.entity.NotificationEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserRepository userRepository;


    // 사용자별 알림 조회
    @GetMapping
    public ResponseEntity<List<NotificationEntity>> getNotificationsByUserId(@RequestParam Long userId) {
        try {
            List<NotificationEntity> notifications = notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 새로운 알림 생성
    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody Map<String, String> notificationRequest) {
        try {
            Long userId = Long.valueOf(notificationRequest.get("userId"));
            String senderName = notificationRequest.getOrDefault("senderName", "System");
            String title = notificationRequest.get("title");
            String message = notificationRequest.get("message");
            String type = notificationRequest.get("type");
            Long senderId = Long.valueOf(notificationRequest.get("senderId"));
            String receivedAt = notificationRequest.get("receivedAt");

            notificationService.createNotification(
                    userId,
                    senderName,
                    title,
                    message,
                    type,
                    senderId,
                    receivedAt);
            return ResponseEntity.ok("알림이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("알림 생성 실패: " + e.getMessage());
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> request) {
        try {
            // senderId 추출
            Long senderId = Long.valueOf(request.get("senderId"));

            // receiverId 추출
            Long receiverId = userRepository.findByUserName(request.get("receiverName"))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다."))
                    .getId();

            // 알림 생성 및 저장
            String message = notificationService.createNotification(
                    receiverId,
                    request.get("senderName"),
                    request.get("title"),
                    request.get("content"),
                    "MESSAGE",
                    senderId,
                    request.get("receivedAt")
            );

            return ResponseEntity.ok(message);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("쪽지 전송 중 문제가 발생했습니다.");
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateNotificationReadStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> updates) {
        boolean isRead = updates.getOrDefault("isRead", false);
        notificationService.updateReadStatus(id, isRead);
        return ResponseEntity.ok("알림 읽음 상태가 업데이트되었습니다.");
    }
    @PatchMapping("/{id}/accept")
    public ResponseEntity<String> acceptMentorRequest(@PathVariable Long id) {
        try {
            notificationService.updateAcceptedStatus(id, true); // 요청 수락 처리
            return ResponseEntity.ok("멘토 요청이 수락되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("알림 수락 처리 중 오류가 발생했습니다.");
        }
    }


}
