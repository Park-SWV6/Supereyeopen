package com.example.demo.service;

import com.example.demo.dto.QuizDTO;
import com.example.demo.entity.QuizEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuizDTO saveQuiz(QuizDTO quizDTO) {
        UserEntity user = userRepository.findById(quizDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + quizDTO.getUserId()));
        QuizEntity quiz = mapToEntity(quizDTO);
        quiz.setUser(user);
        return mapToDTO(quizRepository.save(quiz));
    }

    @Transactional
    public void updateQuizReaction(Long quizId, Long userId, boolean isLike) {
        QuizEntity quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (isLike) {
            if (!quiz.getLikes().contains(user)) {
                quiz.getLikes().add(user); // 좋아요 추가
            }
            quiz.getDislikes().remove(user); // 싫어요 제거
        } else {
            if (!quiz.getDislikes().contains(user)) {
                quiz.getDislikes().add(user); // 싫어요 추가
            }
            quiz.getLikes().remove(user); // 좋아요 제거
        }

        quizRepository.save(quiz);
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

    private QuizEntity mapToEntity(QuizDTO dto) {
        QuizEntity entity = new QuizEntity();
        entity.setId(dto.getId());
        entity.setQuestion(dto.getQuestion());
        entity.setAnswer(dto.getAnswer());
        entity.setDate(dto.getDate());
        entity.setLikes(dto.getLikes().stream().map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id)))
                .collect(Collectors.toList()));
        entity.setDislikes(dto.getDislikes().stream().map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id)))
                .collect(Collectors.toList()));

        return entity;
    }
}
