package com.example.demo.repository;

import com.example.demo.entity.HelpRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRequestRepository extends JpaRepository<HelpRequestEntity, Long> {
}
