package com.example.demo.service;

import com.example.demo.dto.HelpRequestDTO;
import com.example.demo.entity.HelpRequestEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.HelpRequestRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelpRequestService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private String helpRequestImageDir;
    @PostConstruct
    public void init() {
        this.helpRequestImageDir = uploadDir + "help-request-images/";
    }
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public List<HelpRequestDTO> getAllHelpRequests() {
        return helpRequestRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HelpRequestDTO getHelpRequestById(Long id) {
        HelpRequestEntity entity = helpRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HelpRequest not found"));
        return mapToDTO(entity);
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

    @Transactional
    public void deleteHelpRequest(Long id) {
        // 댓글 먼저 삭제
        commentRepository.deleteByPostId(id);

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

    public String uploadHelpRequestImage(Long requestId, MultipartFile file) throws IOException {
        HelpRequestEntity request = helpRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("HelpRequest not found"));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File uploadPath = new File(helpRequestImageDir);

        if(!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            if(!created) {
                throw new IOException("Failed to create upload directory");
            }
        }

        File destinationFile = new File(helpRequestImageDir + fileName);
        file.transferTo(destinationFile);

        List<String> uris = request.getUri();
        uris.add("/uploads/help-request-images/" + fileName);
        request.setUri(uris);

        return "/uploads/help-request-images/" + fileName;
    }

    public void deleteHelpRequestImages(Long requestId, List<String> imageUris) throws IOException {
        HelpRequestEntity request = helpRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Help Request not found"));

        // 파일 삭제
        for (String imageUri: imageUris) {
            String filePath = helpRequestImageDir + imageUri.replace("/uploads/help-request-images/", "");
            File file = new File(filePath);
            if (file.exists() && !file.delete()) {
                throw new IOException("Failed to delete file: " + filePath);
            }
        }

        List<String> updatedUris = request.getUri();
        updatedUris.removeAll(imageUris);
        request.setUri(updatedUris);

        helpRequestRepository.save(request);
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
