package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 특정 채팅방의 메시지 리스트
    @GetMapping("/chat-room/{chatRoomId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId) {
        List<ChatMessageEntity> messages = chatMessageService.getMessages(chatRoomId, userId);
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    // 메시지 전송
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestBody Map<String, Object> messageRequest) {
        Long chatRoomId = Long.valueOf(messageRequest.get("chatRoomId").toString());
        Long senderId = Long.valueOf(messageRequest.get("senderId").toString());
        Long recipientId = Long.valueOf(messageRequest.get("recipientId").toString());
        String content = messageRequest.get("content").toString();
        String timestamp = messageRequest.get("timestamp").toString();
        ChatMessageEntity message = chatMessageService.sendMessage(chatRoomId, senderId, recipientId, content, timestamp);
        // DTO로 변환 후 반환
        ChatMessageDTO messageDTO = new ChatMessageDTO(message);
        return ResponseEntity.ok(messageDTO);
    }

    // 특정 채팅방의 읽지 않은 메시지 수 반환
    @GetMapping("/chat-room/{chatRoomId}/unread-count")
    public ResponseEntity<Integer> getUnreadMessageCount(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId) {
        int unreadCount = chatMessageService.getUnreadMessageCount(chatRoomId, userId);
        return ResponseEntity.ok(unreadCount);
    }
}
