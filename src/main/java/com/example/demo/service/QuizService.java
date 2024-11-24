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
    public void updateQuizLikes(Long id, List<Long> likes, List<Long> dislikes) {
        QuizEntity quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + id));
        quiz.setLikes(likes);
        quiz.setDislikes(dislikes);
        quizRepository.save(quiz);
    }

    private QuizDTO mapToDTO(QuizEntity entity) {
        QuizDTO dto = new QuizDTO();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        dto.setAnswer(entity.getAnswer());
        dto.setLikes(entity.getLikes());
        dto.setDislikes(entity.getDislikes());
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
        entity.setLikes(dto.getLikes());
        entity.setDislikes(dto.getDislikes());
        entity.setDate(dto.getDate());
        return entity;
    }
}
