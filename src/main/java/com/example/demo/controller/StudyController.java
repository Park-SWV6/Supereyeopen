package com.example.demo.controller;

import com.example.demo.entity.StudyDataEntity;
import com.example.demo.entity.SubjectEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.StudyDataRepository;
import com.example.demo.service.StudyService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final JwtUtil jwtUtil;

    private final StudyDataRepository studyDataRepository;

    @PutMapping("/update-time")
    public ResponseEntity<Map<String,Object>> updateStudyTime(
            @RequestBody StudyTimeUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        // 토큰에서 사용자 정보 추출
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        Long userId = jwtUtil.getUserIdFromEmail(email);

        // 학습 데이터 업데이트
        Optional<SubjectEntity> updatedSubject = studyService.updateStudyData(userId, request.getSubjectId(), request.getElapsedSeconds());

        // 사용자 정보 갱신
        UserEntity updatedUser = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("user", updatedUser);
        response.put("updatedSubject", updatedSubject);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/continuous-attendance")
    public ResponseEntity<Integer> getContinuousAttendance(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        Long userId = jwtUtil.getUserIdFromEmail(email);

        int continuousDays = studyService.calculateContinuousAttendanceDays(userId);
        return ResponseEntity.ok(continuousDays);
    }

    @Data
    public static class StudyTimeUpdateRequest {
        private Long subjectId;
        private int elapsedSeconds;
    }

}
