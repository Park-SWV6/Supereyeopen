package com.example.demo.repository;

import com.example.demo.entity.MentorRelationshipsEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorRelationshipRepository extends JpaRepository<MentorRelationshipsEntity, Long> {

    List<MentorRelationshipsEntity> findByMentor(UserEntity mentor);
    List<MentorRelationshipsEntity> findByMentee(UserEntity mentee);
    // 멘토와 멘티 관계가 존재하는지 확인
    Optional<MentorRelationshipsEntity> findByMentorAndMentee(UserEntity mentor, UserEntity mentee);
    // 멘티 ID로 멘토 관계 여부 확인
    boolean existsByMentorIdAndMenteeId(Long mentorId, Long menteeId);
}
