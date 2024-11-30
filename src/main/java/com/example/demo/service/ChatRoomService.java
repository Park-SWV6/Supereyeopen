package com.example.demo.service;

import com.example.demo.entity.ChatRoomEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    // 채팅방 생성
    public ChatRoomEntity createChatRoom(Long mentorId, Long menteeId, String createdAt) {
        // 멘토와 멘티 이름 조회
        UserEntity mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멘토가 존재하지 않습니다."));
        UserEntity mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멘티가 존재하지 않습니다."));

        ChatRoomEntity chatRoom = new ChatRoomEntity();
        chatRoom.setMentorId(mentorId);
        chatRoom.setMenteeId(menteeId);
        chatRoom.setCreatedAt(createdAt);
        chatRoom.setTitle(String.format("%s 님과의 멘토링", mentee.getUserName())); // 멘토 기준 제목
        return chatRoomRepository.save(chatRoom);
    }

    // 사용자별 채팅방 조회
    public List<ChatRoomEntity> getUserChatRooms(Long userId) {
        return chatRoomRepository.findByMentorIdOrMenteeId(userId, userId);
    }
}
