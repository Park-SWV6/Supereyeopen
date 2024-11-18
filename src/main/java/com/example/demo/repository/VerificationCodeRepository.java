package com.example.demo.repository;

import com.example.demo.entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {
    Optional<VerificationCodeEntity> findByEmailAndCode(String email, String code);

    Optional<VerificationCodeEntity> findByEmail(String email);
}
