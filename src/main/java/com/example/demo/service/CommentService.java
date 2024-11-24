package com.example.demo.service;


import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.CommentEntity;
import com.example.demo.entity.HelpRequestEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.HelpRequestRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public CommentDTO addComment(CommentDTO commentDTO) {
        HelpRequestEntity post = helpRequestRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + commentDTO.getPostId()));
        UserEntity user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User  not found: " + commentDTO.getPostId()));

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());
        comment.setDate(commentDTO.getDate());

        CommentEntity savedComment = commentRepository.save(comment);
        return mapToDTO(savedComment);
    }
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

        comment.setContent(commentDTO.getContent());
        comment.setDate(commentDTO.getDate());
        return mapToDTO(commentRepository.save(comment));
    }
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found with ID: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
    private CommentDTO mapToDTO(CommentEntity comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUserName(comment.getUser().getUserName());
        dto.setContent(comment.getContent());
        dto.setDate(comment.getDate());
        return dto;
    }
}
