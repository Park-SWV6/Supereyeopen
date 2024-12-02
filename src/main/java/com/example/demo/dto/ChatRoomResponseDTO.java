package com.example.demo.dto;

import com.example.demo.entity.ChatRoomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatRoomResponseDTO {
    private Long id;
    private String title;
    private String lastMessage;
    private String timestamp;
    private int unreadCount;

    public ChatRoomResponseDTO(ChatRoomEntity chatRoom, int unreadCount) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.lastMessage = chatRoom.getLastMessage();
        this.timestamp = chatRoom.getCreatedAt();
        this.unreadCount = unreadCount;
    }
}
