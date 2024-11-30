package com.example.demo.controller;

import com.example.demo.entity.ChatRoomEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<ChatRoomEntity> createChatRoom(@RequestBody ChatRoomEntity chatRoomRequest) {
        ChatRoomEntity chatRoom = chatRoomService.createChatRoom(chatRoomRequest.getMentorId(), chatRoomRequest.getMenteeId(), chatRoomRequest.getCreatedAt());
        return ResponseEntity.ok(chatRoom);
    }

    // 사용자별 채팅방 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoomEntity>> getUserChatRooms(@PathVariable Long userId) {
        List<ChatRoomEntity> chatRooms = chatRoomService.getUserChatRooms(userId);

        // 동적으로 제목 설정
        List<ChatRoomEntity> updatedChatRooms = chatRooms.stream().peek(chatRoom -> {
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
            chatRoom.setTitle(userTitle);
        }).toList();
        return ResponseEntity.ok(updatedChatRooms);
    }
}
