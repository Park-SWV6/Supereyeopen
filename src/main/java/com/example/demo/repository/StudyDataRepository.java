package com.example.demo.repository;

import com.example.demo.entity.StudyDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyDataRepository extends JpaRepository<StudyDataEntity, Long> {
    Optional<StudyDataEntity> findByUserIdAndDate(Long userId, LocalDate date);

    List<StudyDataEntity> findByUserIdOrderByDateDesc(Long userId);
}
