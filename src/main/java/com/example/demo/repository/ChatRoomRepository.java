package com.example.demo.repository;

import com.example.demo.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    List<ChatRoomEntity> findByMentorIdOrMenteeId(Long mentorId, Long menteeId);
    Optional<ChatRoomEntity> findByMentorRelationshipId(Long relationshipId);
}
