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
    public ChatMessageEntity sendMessage(Long chatRoomId, Long senderId, String content, String timestamp) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        ChatMessageEntity message = new ChatMessageEntity();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(timestamp);

        return chatMessageRepository.save(message);
    }


}
