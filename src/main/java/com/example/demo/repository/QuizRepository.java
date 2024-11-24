package com.example.demo.repository;

import com.example.demo.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<QuizEntity,Long> {
}
