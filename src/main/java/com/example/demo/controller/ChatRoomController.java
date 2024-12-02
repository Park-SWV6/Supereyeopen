package com.example.demo.controller;

import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.entity.ChatRoomEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<ChatRoomEntity> createChatRoom(@RequestBody ChatRoomEntity chatRoomRequest) {
        ChatRoomEntity chatRoom = chatRoomService.createChatRoom(chatRoomRequest.getMentorId(), chatRoomRequest.getMenteeId(), chatRoomRequest.getCreatedAt());
        return ResponseEntity.ok(chatRoom);
    }

    // 사용자별 채팅방 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserChatRooms(@PathVariable Long userId) {
        List<ChatRoomEntity> chatRooms = chatRoomService.getUserChatRooms(userId);

        // 동적으로 제목, 마지막 메시지 및 타임스탬프 처리
        List<Map<String, Object>> updatedChatRooms = chatRooms.stream().map(chatRoom -> {
            String userTitle;
            if (chatRoom.getMentorId().equals(userId)) {
                UserEntity mentee = userRepository.findById(chatRoom.getMenteeId())
                        .orElseThrow(() -> new IllegalArgumentException("멘티 정보 없음"));
                userTitle = String.format("%s 님과의 멘토링", mentee.getUserName());
            } else {
                UserEntity mentor = userRepository.findById(chatRoom.getMentorId())
                        .orElseThrow(() -> new IllegalArgumentException("멘토 정보 없음"));
                userTitle = String.format("%s 님과의 멘토링", mentor.getUserName());
            }

            // 읽지 않은 메시지 수 설정
            long unreadCount = chatMessageRepository.countUnreadMessages(chatRoom.getId(), userId);

            // 마지막 메시지 정보 설정
            ChatMessageEntity lastMessageEntity = chatMessageRepository.findLastMessageByChatRoomId(chatRoom.getId());
            Long lastMessageId = lastMessageEntity != null ? lastMessageEntity.getId() : null;
            String lastMessageContent = lastMessageEntity != null ? lastMessageEntity.getContent() : "메시지가 없습니다.";
            String lastMessageTimestamp = lastMessageEntity != null ? lastMessageEntity.getTimestamp() : null;

            // 명시적으로 HashMap을 사용하여 데이터 구성
            Map<String, Object> chatRoomMap = new HashMap<>();
            chatRoomMap.put("id", chatRoom.getId());
            chatRoomMap.put("title", userTitle);
            chatRoomMap.put("mentorId", chatRoom.getMentorId());
            chatRoomMap.put("menteeId", chatRoom.getMenteeId());
            chatRoomMap.put("createdAt", chatRoom.getCreatedAt());
            chatRoomMap.put("unreadMessages", unreadCount);
            chatRoomMap.put("lastMessageId", lastMessageId);
            chatRoomMap.put("lastMessage", lastMessageContent);
            chatRoomMap.put("lastMessageTimestamp", lastMessageTimestamp);
            chatRoomMap.put("mentorRelationshipId", chatRoom.getMentorRelationship().getId());

            return chatRoomMap;
        }).toList();

        return ResponseEntity.ok(updatedChatRooms);
    }

}
