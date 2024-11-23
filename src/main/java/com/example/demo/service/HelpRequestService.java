package com.example.demo.service;

import com.example.demo.dto.HelpRequestDTO;
import com.example.demo.entity.HelpRequestEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.HelpRequestRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelpRequestService {
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;

    public List<HelpRequestDTO> getAllHelpRequests() {
        return helpRequestRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HelpRequestDTO saveHelpRequest(HelpRequestDTO helpRequestDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        HelpRequestEntity entity = mapToEntity(helpRequestDTO);

        entity.setUser(user);

        HelpRequestEntity savedEntity = helpRequestRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    public HelpRequestDTO updateHelpRequest(Long postId, HelpRequestDTO  updatedRequest, Long userId) {
        HelpRequestEntity existingRequest = helpRequestRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        // 사용자 검증
        if (!existingRequest.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not the owner of this post");
        }

        // 게시물 업데이트
        existingRequest.setTitle(updatedRequest.getTitle());
        existingRequest.setDescription(updatedRequest.getDescription());
        existingRequest.setUri(updatedRequest.getUri());
        HelpRequestEntity updatedEntity = helpRequestRepository.save(existingRequest);
        return mapToDTO(updatedEntity);
    }

    public void deleteHelpRequest(Long id) {
        HelpRequestEntity entity = helpRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + id));

        helpRequestRepository.delete(entity);
    }

    public void updateCommentsCount(Long postId, int delta) {
        HelpRequestEntity helpRequest = helpRequestRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
        helpRequest.setComments(helpRequest.getComments() + delta);
        helpRequestRepository.save(helpRequest);
    }

    private HelpRequestDTO mapToDTO(HelpRequestEntity entity) {
        HelpRequestDTO dto = new HelpRequestDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDate(entity.getDate());
        dto.setComments(entity.getComments());
        dto.setUri(entity.getUri());
        UserEntity user = entity.getUser();
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return dto;
    }

    private HelpRequestEntity mapToEntity(HelpRequestDTO dto) {
        HelpRequestEntity entity = new HelpRequestEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setComments(dto.getComments());
        entity.setUri(dto.getUri());
        return entity;
    }
}
