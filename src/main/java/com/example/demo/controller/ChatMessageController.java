package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 특정 채팅방의 메시지 조회
    @GetMapping("/chat-room/{chatRoomId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageDTO> messages = chatMessageService.getMessages(chatRoomId);
        return ResponseEntity.ok(messages);
    }

    // 메시지 전송
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestBody Map<String, Object> messageRequest) {
        Long chatRoomId = Long.valueOf(messageRequest.get("chatRoomId").toString());
        Long senderId = Long.valueOf(messageRequest.get("senderId").toString());
        String content = messageRequest.get("content").toString();
        String timestamp = messageRequest.get("timestamp").toString();
        ChatMessageEntity message = chatMessageService.sendMessage(chatRoomId, senderId, content, timestamp);
        // DTO로 변환 후 반환
        ChatMessageDTO messageDTO = new ChatMessageDTO(message);
        return ResponseEntity.ok(messageDTO);
    }
}
