package com.example.demo.repository;

import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatRoomIdOrderByTimestampAsc(Long chatRoomId);
    List<ChatMessageEntity> findByChatRoom(ChatRoomEntity chatRoom);



    // 특정 사용자가 읽지 않은 메시지 수 반환
    @Query("SELECT COUNT(m) FROM ChatMessageEntity m WHERE m.chatRoom.id = :chatRoomId AND m.recipient.id = :userId AND m.isRead = false")
    int countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

    // 특정 채팅방의 메시지 가져오기 (최신순)
    @Query("SELECT m FROM ChatMessageEntity m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.timestamp ASC")
    List<ChatMessageEntity> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.chatRoom.id = :chatRoomId ORDER BY cm.timestamp DESC LIMIT 1")
    ChatMessageEntity findLastMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);
}
