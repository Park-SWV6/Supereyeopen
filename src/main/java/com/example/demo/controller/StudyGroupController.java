package com.example.demo.controller;

import com.example.demo.dto.StudyGroupDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.StudyGroupEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.StudyGroupRepository;
import com.example.demo.service.StudyGroupService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/study-groups")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final StudyGroupRepository studyGroupRepository;

    @GetMapping
    public ResponseEntity<List<StudyGroupDTO>> getAllGroups() {
        List<StudyGroupDTO> groups = studyGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping
    public ResponseEntity<StudyGroupDTO> createGroup(
            @RequestBody StudyGroupDTO studyGroupRequest,
            @RequestHeader("Authorization") String authHeader) {
        // JWT 토큰에서 사용자 이메일 추출
        String email = jwtUtil.extractEmail(authHeader.substring(7));
        UserEntity leader = userService.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // DTO를 엔티티로 변환 및 그룹 생성
        StudyGroupEntity newGroup = studyGroupService.createGroup(studyGroupRequest, leader);

        // 생성된 그룹 정보를 DTO로 변환하여 반환
        StudyGroupDTO responseDTO = studyGroupService.mapToDTO(newGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<StudyGroupDTO> updateGroup(
            @PathVariable Long groupId,
            @RequestBody StudyGroupDTO updatedGroupRequest) {
        // 그룹 수정 및 반환
        StudyGroupEntity updatedGroup = studyGroupService.updateGroup(groupId, updatedGroupRequest);
        StudyGroupDTO responseDTO = studyGroupService.mapToDTO(updatedGroup);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        studyGroupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<UserDTO> joinGroup(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmail(authHeader.substring(7));
        UserEntity user = userService.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        studyGroupService.addMember(groupId, user.getId());

        return ResponseEntity.ok(new UserDTO(user));
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<String> leaveGroup(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmail(authHeader.substring(7));
        UserEntity user = userService.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        studyGroupService.removeMember(groupId, user.getId());
        // 그룹이 존재하는지 확인
        boolean groupExists = studyGroupRepository.existsById(groupId);

        return ResponseEntity.ok(groupExists ? "Left successfully" : "Group deleted successfully");
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<UserDTO>> getGroupMembers(@PathVariable Long groupId) {
        List<UserDTO> members = studyGroupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }
    /**
     * 스터디 그룹 이미지 업로드
     * @param groupId 스터디 그룹 ID
     * @param file 업로드할 이미지 파일
     * @param authHeader 인증 헤더 (JWT 토큰)
     * @return 업로드된 이미지의 URI
     */
    @PostMapping("/{groupId}/upload-group-image")
    public ResponseEntity<Map<String, String>> uploadGroupImage(
            @PathVariable Long groupId,
            @RequestPart("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String imageUri = studyGroupService.uploadGroupImage(groupId, file);

            Map<String, String> response = new HashMap<>();
            response.put("imageUri", imageUri);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 그룹 ID가 잘못된 경우 404 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));

        } catch (IOException e) {
            // 파일 업로드 중 문제 발생 시 500 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }
}
