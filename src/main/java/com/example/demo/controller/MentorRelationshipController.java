package com.example.demo.controller;


import com.example.demo.entity.MentorRelationshipsEntity;
import com.example.demo.service.MentorRelationshipService;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mentor-relationships")
@RequiredArgsConstructor
public class MentorRelationshipController {
    private final MentorRelationshipService mentorRelationshipService;
    private final NotificationService notificationService;

    // 멘토 요청 API
    @PostMapping("/request")
    public ResponseEntity<String> requestMentor(@RequestBody Map<String, String> request) {
        try {
            Long senderId = Long.valueOf(request.get("senderId"));
            String senderName = request.get("senderName");
            Long receiverId = Long.valueOf(request.get("receiverId"));
            String receivedAt = request.get("receivedAt");
            String message = mentorRelationshipService.requestMentor(senderId, senderName, receiverId, receivedAt);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 멘토 관계 생성 API (수락)
    @PostMapping("/create")
    public ResponseEntity<String> createMentorRelationship(@RequestBody Map<String, String> request) {
        try {
            Long mentorId = Long.valueOf(request.get("mentorId"));
            Long menteeId = Long.valueOf(request.get("menteeId"));
            String createdAt = request.get("createdAt");
            String message = mentorRelationshipService.createMentorRelationship(mentorId, menteeId, createdAt);

            // 알림 생성: 멘토에게 관계 수락 알림 전송
            notificationService.createNotification(
                    mentorId, // 수신자 ID
                    "System", // 발신자 이름 (시스템 알림)
                    "멘토 요청 수락", // 알림 제목
                    "사용자가 멘토 요청을 수락했습니다.", // 알림 메시지
                    "MENTOR_REQUEST_ACCEPTED", // 알림 타입
                    menteeId,// 발신자 ID,
                    createdAt
            );
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 멘토 리스트 조회 API
    @GetMapping("/mentors/{menteeId}")
    public ResponseEntity<List<MentorRelationshipsEntity>> getMentorList(@PathVariable Long menteeId) {
        try {
            List<MentorRelationshipsEntity> mentors = mentorRelationshipService.getMentorList(menteeId);
            return ResponseEntity.ok(mentors);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 멘티 리스트 조회 API
    @GetMapping("/mentees/{mentorId}")
    public ResponseEntity<List<MentorRelationshipsEntity>> getMenteeList(@PathVariable Long mentorId) {
        try {
            List<MentorRelationshipsEntity> mentees = mentorRelationshipService.getMenteeList(mentorId);
            return ResponseEntity.ok(mentees);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    // 특정 게시물 작성자와 사용자 간의 멘토 관계 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkMentorRelationship(@RequestParam Long postId, @RequestParam Long userId) {
        try {
            boolean isMentorRelationship = mentorRelationshipService.isMentorRelationship(userId, postId);
            return ResponseEntity.ok(isMentorRelationship);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @DeleteMapping("/{relationshipId}/end")
    public ResponseEntity<Void> endMentoring(@PathVariable Long relationshipId, @RequestBody Map<String ,String> request) {
        mentorRelationshipService.endMentoring(relationshipId, request.get("receivedAt"));
        return ResponseEntity.ok().build();
    }
}
