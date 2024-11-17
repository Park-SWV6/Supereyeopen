package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 사용자 이름으로 사용자 검색
    Optional<UserEntity> findByUserName(String userName);

    // 이메일로 사용자 검색 (회원가입 시 중복 확인용)
    Optional<UserEntity> findByUserEmail(String userEmail);
}
