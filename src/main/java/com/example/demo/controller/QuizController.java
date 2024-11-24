package com.example.demo.controller;

import com.example.demo.dto.QuizDTO;
import com.example.demo.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

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

    @PutMapping("/{id}/reactions")
    public ResponseEntity<?> updateQuizLikes(
            @PathVariable Long id,
            @RequestBody Map<String, List<Long>> likesAndDislikes
    ) {
        try {
            quizService.updateQuizLikes(
                    id,
                    likesAndDislikes.get("likes"),
                    likesAndDislikes.get("dislikes")
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
