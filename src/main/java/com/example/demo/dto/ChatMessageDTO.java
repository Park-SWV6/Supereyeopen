package com.example.demo.dto;

import com.example.demo.entity.ChatMessageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessageDTO {
    private Long id;
    private String content;
    private String timestamp;
    private Long chatRoomId;
    private String senderName; // 보낸 사람의 이름 추가
    private Long senderId;
    private Long recipientId;

    public ChatMessageDTO(ChatMessageEntity entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.timestamp = entity.getTimestamp();
        this.chatRoomId = entity.getChatRoom().getId();
        this.senderName = entity.getSender().getUserName(); // sender의 userName 가져오기
        this.senderId = entity.getSender().getId();
        this.recipientId = entity.getRecipient().getId();
    }

}
