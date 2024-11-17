package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //비밀번호 암호화
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

//    public Long save(UserDTO userDTO){
//        userRepository.save(UserEntity.toSaveEntity(userDTO));
//        UserEntity userEntity = UserEntity.toSaveEntity(userDTO);
//        Long savedid= userRepository.save(userEntity).getId();
//        return savedid;
//    }
    public Long registerUser(UserDTO userDTO) {
        // 중복 검사
        if (userRepository.findByUserEmail(userDTO.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }
        UserEntity userEntity = toSaveEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        return savedUser.getId();
    }

    public UserEntity loginUser(String userEmail, String userPassword) {
        Optional<UserEntity> userOptional = userRepository.findByUserEmail(userEmail);

        if (userOptional.isEmpty() || !userOptional.get().getUserPassword().equals(userPassword)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return userOptional.get();
    }

    private UserEntity toSaveEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserEmail(userDTO.getUserEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(userDTO.getUserPassword()));
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setProfileImageUri(null); // 초기엔 없음
        userEntity.setStudyGroupId(null); // 초기엔 없음
        userEntity.setStudyTime(0); // 최초엔 0
        userEntity.setHelpGivenCount(0);
        userEntity.setHelpReceivedCount(0);
        return userEntity;
    }

    // 사용자 검색 메서드
    public Optional<UserEntity> findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }
}
