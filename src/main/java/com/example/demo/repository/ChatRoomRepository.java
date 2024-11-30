package com.example.demo.repository;

import com.example.demo.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    List<ChatRoomEntity> findByMentorIdOrMenteeId(Long mentorId, Long menteeId);
}
