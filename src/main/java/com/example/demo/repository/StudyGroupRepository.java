package com.example.demo.repository;

import com.example.demo.dto.StudyGroupDTO;
import com.example.demo.entity.StudyGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroupEntity, Long> {

    // 특정 사용자가 속한 스터디 그룹 조회
    List<StudyGroupEntity> findByLeaderId(Long leaderId);

    // 그룹 이름으로 검색 (부분 검색 지원)
    List<StudyGroupEntity> findByNameContainingIgnoreCase(String name);

    // 사용자가 속한 그룹 검색
    List<StudyGroupEntity> findByMembers_Id(Long userId);

    // 리더 ID로 스터디 그룹 조회
    List<StudyGroupEntity> findAllByLeaderId(Long leaderId);

    @Query("SELECT new com.example.demo.dto.StudyGroupDTO(" +
            "sg.id, sg.name, sg.description, sg.limit, sg.membersCount, sg.imageUri, " +
            "leader.id, leader.userName) " +
            "FROM StudyGroupEntity sg " +
            "LEFT JOIN sg.leader leader")
    List<StudyGroupDTO> findAllGroupsWithLeader();


}
