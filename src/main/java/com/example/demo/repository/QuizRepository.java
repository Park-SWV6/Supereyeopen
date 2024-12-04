package com.example.demo.repository;

import com.example.demo.dto.QuizDTO;
import com.example.demo.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizEntity,Long> {
    @Query("SELECT q FROM QuizEntity q WHERE SIZE(q.likes) - SIZE(q.dislikes) >= :threshold")
    List<QuizEntity> findByLikesMinusDislikesGreaterThan(@Param("threshold") int threshold);

}
