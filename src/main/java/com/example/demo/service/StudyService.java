package com.example.demo.service;

import com.example.demo.entity.StudyDataEntity;
import com.example.demo.entity.SubjectEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.StudyDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyDataRepository studyDataRepository;
    private final SubjectService subjectService;
    private final UserService userService;

    /**
     * 특정 과목과 사용자로부터 학습 시간을 업데이트하고 StudyDataEntity에 반영
     */
    @Transactional
    public Optional<SubjectEntity> updateStudyData(Long userId, Long subjectId, int elapsedSeconds) {
        // 과목 시간 업데이트
        SubjectEntity subject = subjectService.updateSubjectTime(subjectId, elapsedSeconds);

        // 사용자 시간 업데이트
        UserEntity user = subject.getUser();
        int previousStudyTime = user.getStudyTime();
        user.setStudyTime(previousStudyTime + elapsedSeconds);
        System.out.println("Updated user.studyTime from "  + previousStudyTime + " to " + user.getStudyTime());

        // StudyDataEntity 업데이트
        LocalDate today = LocalDate.now();
        StudyDataEntity studyData = studyDataRepository
                .findByUserIdAndDate(user.getId(), today)
                .orElse(new StudyDataEntity());

        studyData.setUser(user);
        studyData.setDate(today);
        studyData.setStudyTime(studyData.getStudyTime() + elapsedSeconds);
        studyDataRepository.save(studyData);

        userService.save(user);

        // 업데이트된 SubjectEntity 반환
        return Optional.of(subject);
    }

    /**
     * 사용자의 연속 출석 일수 계산
     */
    public int calculateContinuousAttendanceDays(Long userId) {
        List<StudyDataEntity> studyDataList = studyDataRepository.findByUserIdOrderByDateDesc(userId);

        if (studyDataList.isEmpty()) {
            return 0;
        }

        int continuousDays = 1;
        LocalDate today = LocalDate.now();

        // 오늘 포함 여부 확인
        if (!studyDataList.get(0).getDate().equals(today)) {
            return 0; // 오늘 기록이 없으면 연속 출석 없음
        }

        for (int i = 0; i < studyDataList.size() - 1; i++) {
            LocalDate currentDate = studyDataList.get(i).getDate();
            LocalDate previousDate = studyDataList.get(i + 1).getDate();

            if (currentDate.minusDays(1).equals(previousDate)) {
                continuousDays++;
            } else {
                break; // 연속성이 끊기면 중단
            }
        }

        return continuousDays;
    }
}