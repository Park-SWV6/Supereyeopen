package com.example.demo.service;


import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.entity.ChatRoomEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public List<ChatMessageDTO> getMessages(Long chatRoomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));

        List<ChatMessageEntity> entities = chatMessageRepository.findByChatRoom(chatRoom);
        return entities.stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());
    }

    // 메시지 전송
    public ChatMessageEntity sendMessage(Long chatRoomId, Long senderId, Long recipientId, String content, String timestamp) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        ChatMessageEntity message = new ChatMessageEntity();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(timestamp);
        message.setRecipient(userRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("수신자를 찾을 수 없습니다.")));

        return chatMessageRepository.save(message);
    }

    // 읽지 않은 메시지 수 반환
    public int getUnreadMessageCount(Long chatRoomId, Long userId) {
        return chatMessageRepository.countUnreadMessages(chatRoomId, userId);
    }

    // 특정 채팅방의 모든 메시지 가져오기
    public List<ChatMessageEntity> getMessages(Long chatRoomId, Long userId) {
        List<ChatMessageEntity> messages = chatMessageRepository.findByChatRoomId(chatRoomId);

        // 사용자가 읽지 않은 메시지를 읽음 처리
        for (ChatMessageEntity message : messages) {
            if (!message.isRead() && message.getRecipient().getId().equals(userId)) {
                message.setRead(true); // 읽음 처리
            }
        }
        chatMessageRepository.saveAll(messages); // 읽음 상태 저장
        return messages;
    }
}
