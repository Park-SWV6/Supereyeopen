package com.example.demo.entity;

import com.example.demo.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    //@Column(name="user_id")
    private Long id;

    @Column(length=50, unique = true, nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(length = 20, nullable = false, unique = true) // 중복 불가, 필수 입력
    private String userName;

    @Column(nullable = true)
    private String profileImageUri; // 프로필 이미지 URL

    @Column(nullable = true)
    private Long studyGroupId; // Foreign Key

    @Column(nullable = false)
    private int studyTime;

    @Column(nullable = false)
    private int helpGivenCount;

    @Column(nullable = false)
    private int helpReceivedCount;

//    @Column
//    private int memberAge;

//    public static UserEntity toSaveEntity(UserDTO userDTO){
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserEmail(userDTO.getUserEmail());
//        userEntity.setUserPassword(userDTO.getUserPassword());
//        userEntity.setUserName(userDTO.getUserName());
//        return userEntity;
//    }
//
//
//    private void setUserEmail(Object userEmail) {
//        Object Email;
//    }
//    private void setUserPassword(Object userPassword) {
//        Object Password;
//    }
//    private void setUserName(Object userName){
//        Object Name;
//    }
//    private void setMemberAge(Object memberAge){
//        Object Age;
//    }
//
//    public Long getId() {
//        return null;
//    }
}

