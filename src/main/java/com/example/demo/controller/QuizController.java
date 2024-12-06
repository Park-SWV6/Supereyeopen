package com.example.demo.controller;

import com.example.demo.dto.QuizDTO;
import com.example.demo.entity.QuizEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.QuizRepository;
import com.example.demo.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final QuizRepository quizRepository;
    @GetMapping
    public List<QuizDTO> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDTO quizDTO) {
        try {
            QuizDTO createdQuiz = quizService.saveQuiz(quizDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{quizId}/react")
    public ResponseEntity<?> updateQuizReactions(
            @PathVariable Long quizId,
            @RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            boolean isLike = Boolean.parseBoolean(payload.get("isLike").toString());

            quizService.updateQuizReaction(quizId, userId, isLike);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/top")
    public ResponseEntity<QuizDTO> getRandomTopQuiz() {
        List<QuizEntity> topQuizzes = quizRepository.findByLikesMinusDislikesGreaterThan(20);

        // QuizEntity -> QuizDTO로 변환
        List<QuizDTO> topQuizDTOs = topQuizzes.stream()
                .map(this::mapToDTO) // mapToDTO 메서드를 사용하여 변환
                .toList();

        if (topQuizzes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        QuizDTO randomQuiz = topQuizDTOs.get(ThreadLocalRandom.current().nextInt(topQuizDTOs.size()));
        return ResponseEntity.ok(randomQuiz);
    }

    private QuizDTO mapToDTO(QuizEntity entity) {
        QuizDTO dto = new QuizDTO();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        dto.setAnswer(entity.getAnswer());
        dto.setLikes(entity.getLikes().stream().map(UserEntity::getId).collect(Collectors.toList()));
        dto.setDislikes(entity.getDislikes().stream().map(UserEntity::getId).collect(Collectors.toList()));
        dto.setDate(entity.getDate());

        UserEntity user = entity.getUser();
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return dto;
    }
}
