package com.example.demo.service;

import com.example.demo.dto.StudyGroupDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.StudyGroupEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.StudyGroupRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final GcsService gcsService;

    @Transactional
    public List<StudyGroupDTO> getAllGroups() {
        return studyGroupRepository.findAllGroupsWithLeader();
    }

    public StudyGroupEntity createGroup(StudyGroupDTO studyGroupRequest, UserEntity leader) {
        // DTO를 엔티티로 변환
        StudyGroupEntity entity = new StudyGroupEntity();
        entity.setName(studyGroupRequest.getName());
        entity.setDescription(studyGroupRequest.getDescription());
        entity.setLimit(studyGroupRequest.getLimit());
        entity.setImageUri(studyGroupRequest.getImageUri());
        entity.setLeader(leader);
        entity.setMembersCount(1); // 리더 포함 초기 멤버 수 설정

        StudyGroupEntity savedGroup = studyGroupRepository.save(entity);
        // 리더의 studyGroup 관계 설정
        leader.setStudyGroup(savedGroup);
        userRepository.save(leader); // 리더 정보도 저장

        return savedGroup;
    }

    @Transactional
    public StudyGroupEntity updateGroup(Long groupId, StudyGroupDTO updatedGroupRequest) {
        StudyGroupEntity group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 수정 가능한 필드 업데이트
        group.setName(updatedGroupRequest.getName());
        group.setDescription(updatedGroupRequest.getDescription());
        group.setLimit(updatedGroupRequest.getLimit());
        group.setImageUri(updatedGroupRequest.getImageUri());

        // 그룹 제한 인원이 줄어들 경우 멤버 수를 초과하지 않도록 처리
        if (group.getMembersCount() > updatedGroupRequest.getLimit()) {
            throw new IllegalStateException("Limit cannot be less than current members count.");
        }


        return studyGroupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        StudyGroupEntity group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 스터디 그룹의 모든 멤버 관계 초기화
        group.getMembers().forEach(member -> {
            member.setStudyGroup(null); // 관계 초기화
            userRepository.save(member); // 변경 사항 저장
        });

        // 멤버 목록 초기화
        group.getMembers().clear();

        // 리더와의 관계 초기화
        if (group.getLeader() != null) {
            UserEntity leader = group.getLeader();
            leader.setStudyGroup(null); // 관계 초기화
            userRepository.save(leader); // 변경 사항 저장
        }

        // 그룹 삭제
        group.setLeader(null);
        studyGroupRepository.delete(group);
    }



    @Transactional
    public void addMember(Long groupId, Long userId) {
        StudyGroupEntity group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (group.getMembersCount() >= group.getLimit()) {
            throw new IllegalStateException("Group is full");
        }

        user.setStudyGroup(group);
        group.getMembers().add(user);
        group.setMembersCount(group.getMembersCount() + 1);

        userRepository.save(user);
        studyGroupRepository.save(group);
    }

    @Transactional
    public void removeMember(Long groupId, Long userId) {
        StudyGroupEntity group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (group.getLeader() != null && group.getLeader().getId().equals(userId)) {
            if (group.getMembers().size() > 1) {
                // 리더를 다른 멤버로 지정
                UserEntity newLeader = group.getMembers().stream()
                        .filter(member -> !member.getId().equals(user.getId())) // 리더 본인 제외
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No other members available to become leader"));

                group.setLeader(newLeader);
            } else {
                // 그룹 삭제
                studyGroupRepository.delete(group);
                return;
            }
        }
        user.setStudyGroup(null); // 사용자와 그룹의 관계 제거
        group.getMembers().remove(user);
        group.setMembersCount(group.getMembersCount() - 1);

        userRepository.save(user); // 그룹 상태 저장
        studyGroupRepository.save(group); // 사용자 상태 저장
    }

    @Transactional
    public List<UserDTO> getGroupMembers(Long groupId) {
        StudyGroupEntity group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return group.getMembers().stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    // 스터디 그룹 이미지 업로드
    public String uploadGroupImage(Long groupId, MultipartFile file) throws IOException {
        StudyGroupEntity group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("StudyGroup not found"));

        // 기존 그룹 이미지 URI 삭제
        String existingGroupImageUri = group.getImageUri();
        if (existingGroupImageUri != null && !existingGroupImageUri.isEmpty()) {
            String existingFileName = existingGroupImageUri.substring(existingGroupImageUri.lastIndexOf("/") + 1);
            try {
                gcsService.deleteFile("group-images", existingFileName);  // 기존 파일 삭제
            } catch (Exception e) {
                throw new IOException("Failed to delete existing StudyGroup image: " + existingFileName, e);
            }
        }

        // GCS에 업로드할 파일 이름 생성
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // GCS에 파일 업로드
        String fileUri = gcsService.uploadFile("group-images", fileName, file.getBytes());

        group.setImageUri(fileUri);
        studyGroupRepository.save(group);

        return group.getImageUri();
    }

    public StudyGroupDTO mapToDTO(StudyGroupEntity entity) {
        StudyGroupDTO dto = new StudyGroupDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setLimit(entity.getLimit());
        dto.setMembersCount(entity.getMembersCount());
        dto.setImageUri(entity.getImageUri());

        if (entity.getLeader() != null) {
            dto.setLeaderName(entity.getLeader().getUserName());
            dto.setLeaderId(entity.getLeader().getId());
        }

        return dto;
    }

    private UserDTO mapToUserDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserName(user.getUserName());
        dto.setProfileImageUri(user.getProfileImageUri());
        dto.setStudyGroupId(user.getStudyGroupId()); // studyGroupId 반영
        dto.setStudyTime(user.getStudyTime());
        dto.setHelpGivenCount(user.getHelpGivenCount());
        dto.setHelpReceivedCount(user.getHelpReceivedCount());
        return dto;
    }
}
