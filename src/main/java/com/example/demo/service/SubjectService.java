package com.example.demo.service;


import com.example.demo.entity.SubjectEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserService userService;

    // 특정 사용자의 모든 과목 조회
    public List<SubjectEntity> getSubjectByUserId(Long userId) {
        return subjectRepository.findByUserId(userId);
    }

    // 과목 업데이트
    public SubjectEntity updateSubjectTime(Long subjectId, int elapsedSeconds) {
        SubjectEntity subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + subjectId));

        subject.setTime(subject.getTime() + elapsedSeconds);
        SubjectEntity updatedSubject = subjectRepository.save(subject);

        // 사용자 총 공부 시간 업데이트
        UserEntity user = subject.getUser();
        user.setStudyTime(user.getStudyTime() + elapsedSeconds); // 초 단위 누적
        userService.save(user);

        return subjectRepository.save(subject);
    }

    public SubjectEntity saveSubject(SubjectEntity subject) {
        return subjectRepository.save(subject);
    }

    public Optional<SubjectEntity> findById(Long id) {
        return subjectRepository.findById(id);
    }

    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }
}
