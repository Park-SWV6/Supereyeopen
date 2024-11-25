package com.example.demo.controller;


import com.example.demo.entity.SubjectEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.SubjectService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final JwtUtil jwtUtil;

    // 특정 사용자의 과목 리스트 조회
    @GetMapping
    public ResponseEntity<List<SubjectEntity>> getSubjects(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        Long userId = jwtUtil.getUserIdFromEmail(email); // 이메일로 사용자 id 추출
        List<SubjectEntity> subjects = subjectService.getSubjectByUserId(userId);

        return ResponseEntity.ok(subjects);
    }

    // 과목 시간 업데이트
    @PutMapping("/{subjectId}/update-time")
    public ResponseEntity<SubjectEntity> updateSubjectTime(
            @PathVariable Long subjectId,
            @RequestBody TimeUpdateRequest request) {

        // 초 단위로 저장
        int elapsedSeconds = request.getElapsedTime();
        SubjectEntity updatedSubject = subjectService.updateSubjectTime(subjectId, elapsedSeconds);

        // API 응답에서는 문자열 형태로 반환
        updatedSubject.setTimeAsString(formatTime(updatedSubject.getTime()));
        return ResponseEntity.ok(updatedSubject);
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // 과목 추가 API
    @PostMapping
    public ResponseEntity<SubjectEntity> addSubject(
            @RequestBody SubjectEntity subject,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.extractEmail(authHeader.substring(7));
        UserEntity user = userService.findByUserEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        subject.setUser(user);
        SubjectEntity savedSubject = subjectService.saveSubject(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);
    }

    // 과목 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<SubjectEntity> updateSubject(
            @PathVariable Long id,
            @RequestBody SubjectEntity updatedSubject
    ) {
        SubjectEntity subject = subjectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        subject.setTitle(updatedSubject.getTitle());
        subject.setTime(updatedSubject.getTime());

        SubjectEntity savedSubject =subjectService.saveSubject(subject);
        return ResponseEntity.ok(savedSubject);
    }

    //과목 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubjectById(id);
        return ResponseEntity.noContent().build();
    }


    // 과목 시간 업데이트 요청 DTO
    public static class TimeUpdateRequest {
        private int elapsedTime;

        public int getElapsedTime() {
            return elapsedTime;
        }

        public void setElapsedTime(int elapsedTime) {
            this.elapsedTime = elapsedTime;
        }
    }
}
