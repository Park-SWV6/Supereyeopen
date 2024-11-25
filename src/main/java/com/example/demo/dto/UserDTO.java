package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO {
    private Long id;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String profileImageUri;
    private Long studyGroupId;
    private String studyGroupName;
    private int studyTime;
    private int helpGivenCount;
    private int helpReceivedCount;
    // 생성자: UserEntity와 관련된 데이터로 초기화
    public UserDTO(UserEntity user) {
        this.id = user.getId();
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.profileImageUri = user.getProfileImageUri();

        // studyGroup 정보가 존재할 경우 studyGroupId 초기화
        if (user.getStudyGroup() != null) {
            this.studyGroupId = user.getStudyGroup().getId();
            this.studyGroupName = user.getStudyGroup().getName();
        }

        this.studyTime = user.getStudyTime();
        this.helpGivenCount = user.getHelpGivenCount();
        this.helpReceivedCount = user.getHelpReceivedCount();
    }
    public UserDTO() {}
//    private int memberAge;
// memberAge가 필요한가요?
//    public Object getMemberEmail() {
//        return null;
//    }
//
//    public Object getMemberPassword() {
//        return null;
//    }
//
//    public Object getMemberName() {
//        return null;
//    }

//    public Object getMemberAge() {
//        return null;
//    }
}
