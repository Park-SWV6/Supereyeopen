package com.example.demo.service;


import com.example.demo.entity.MentorRelationshipsEntity;
import com.example.demo.entity.NotificationEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.MentorRelationshipRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorRelationshipService {
    private final MentorRelationshipRepository mentorRelationshipRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

    // 멘토 관계 요청 생성
    public String requestMentor(Long senderId, String senderName, Long recipientId) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("발신자가 존재하지 않습니다."));
        UserEntity recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("수신자가 존재하지 않습니다."));

        // 중복 요청 방지
        if (mentorRelationshipRepository.findByMentorAndMentee(recipient, sender).isPresent()) {
            throw new IllegalArgumentException("이미 멘토 관계가 존재합니다.");
        }

        // 알림 생성
        NotificationEntity notification = new NotificationEntity();
        notification.setSenderId(senderId);
        notification.setReceiverId(recipientId);
        notification.setSenderName("System");
        notification.setTitle("멘토 요청");
        notification.setMessage(senderName + "님이 멘토 관계를 요청했습니다.");
        notification.setType("MENTOR_REQUEST");
        notificationRepository.save(notification);

        return "멘토 요청이 성공적으로 전송되었습니다.";
    }

    // 멘토 관계 생성 (수락)
    public String createMentorRelationship(Long mentorId, Long menteeId, String createdAt) {
        if (mentorRelationshipRepository.findByMentorAndMentee(
                userRepository.findById(mentorId).orElseThrow(() -> new IllegalArgumentException("멘토 ID가 존재하지 않습니다.")),
                userRepository.findById(menteeId).orElseThrow(() -> new IllegalArgumentException("멘티 ID가 존재하지 않습니다."))
        ).isPresent()) {
            throw new IllegalArgumentException("이미 멘토 관계가 존재합니다.");
        }

        MentorRelationshipsEntity relationship = new MentorRelationshipsEntity();
        relationship.setMentor(userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("멘토 ID가 존재하지 않습니다.")));
        relationship.setMentee(userRepository.findById(menteeId)
                .orElseThrow(() -> new IllegalArgumentException("멘티 ID가 존재하지 않습니다.")));
        relationship.setCreatedAt(createdAt);
        mentorRelationshipRepository.save(relationship);

        chatRoomService.createChatRoom(mentorId, menteeId, createdAt);

        return "멘토 관계가 성공적으로 생성되었습니다.";
    }

    // 멘토 리스트 조회
    public List<MentorRelationshipsEntity> getMentorList(Long menteeId) {
        UserEntity mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new IllegalArgumentException("멘티 ID가 존재하지 않습니다."));
        return mentorRelationshipRepository.findByMentee(mentee);
    }

    // 멘티 리스트 조회
    public List<MentorRelationshipsEntity> getMenteeList(Long mentorId) {
        UserEntity mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("멘토 ID가 존재하지 않습니다."));
        return mentorRelationshipRepository.findByMentor(mentor);
    }

    // 특정 게시물 작성자와 사용자 간의 멘토 관계 확인
    public boolean isMentorRelationship(Long userId, Long postId) {

        // 멘토 관계가 성립했는지 확인
        return mentorRelationshipRepository.existsByMentorIdAndMenteeId(userId, postId);
    }
}
