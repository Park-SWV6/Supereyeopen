package com.example.demo.controller;


import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.CommentEntity;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDTO> getCommentsByPostId(@RequestParam Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping
    public CommentDTO addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PutMapping("/{id}")
    public CommentDTO updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
