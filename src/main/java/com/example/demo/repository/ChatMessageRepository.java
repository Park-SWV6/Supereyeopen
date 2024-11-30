package com.example.demo.repository;

import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatRoomIdOrderByTimestampAsc(Long chatRoomId);
    List<ChatMessageEntity> findByChatRoom(ChatRoomEntity chatRoom);
}
