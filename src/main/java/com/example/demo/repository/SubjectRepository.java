package com.example.demo.repository;

import com.example.demo.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends  JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findByUserId(Long userId); // 특정 사용자의 과목 리스트 조회
}

