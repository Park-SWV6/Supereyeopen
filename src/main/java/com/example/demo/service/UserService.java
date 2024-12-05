package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.VerificationCodeEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //비밀번호 암호화
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //메일 전송
    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    // Google GCS
    private final GcsService gcsService;

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
        UserEntity userEntity = new UserEntity();
        userEntity.setUserEmail(userDTO.getUserEmail());
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(userDTO.getUserPassword()));
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setProfileImageUri(null);
        userEntity.setStudyTime(0);
        userEntity.setHelpGivenCount(0);
        userEntity.setHelpReceivedCount(0);

        UserEntity savedUser = userRepository.save(userEntity);
        return savedUser.getId();
    }

    public UserDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToUserDTO(user);
    }

    public UserEntity loginUser(String userEmail, String userPassword) {
        Optional<UserEntity> userOptional = userRepository.findByUserEmail(userEmail);

        if (userOptional.isEmpty() || !userOptional.get().getUserPassword().equals(userPassword)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return userOptional.get();
    }


    // 사용자 검색 메서드
    public Optional<UserEntity> findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    public void sendVerificationCode(String email) {
        // 인증번호 생성 (6자리 숫자)
        String verificationCode = String.valueOf((int)((Math.random() * 900000) + 100000));


        // 기존에 해당 이메일로 저장된 인증번호가 있는지 확인하고 삭제
        Optional<VerificationCodeEntity> existingVerificationCode = verificationCodeRepository.findByEmail(email);
        existingVerificationCode.ifPresent(verificationCodeRepository::delete);

        // 인증번호 저장 (일정 시간 동안 유효)
        VerificationCodeEntity verificationCodeEntity = new VerificationCodeEntity(email, verificationCode, new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // 5분 유효
        verificationCodeRepository.save(verificationCodeEntity);

        // 이메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("이메일 인증 요청");
        mailMessage.setText("인증번호: " + verificationCode);
        mailSender.send(mailMessage);
    }

    public boolean verifyCode(String email, String code) {
        Optional<VerificationCodeEntity> verificationCode = verificationCodeRepository.findByEmailAndCode(email, code);

        if(verificationCode.isPresent() && verificationCode.get().getExpiryDate().after(new Date())) {
            // 인증 성공 후 VerificationCode 삭재 (재사용 방지)
            verificationCodeRepository.delete(verificationCode.get());
            return true;
        }
        return false;
    }
    // 사용자 ID로 조회
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id); // JpaRepository의 기본 메서드 호출
    }

    // 사용자 저장
    public UserEntity save(UserEntity user) {
        return userRepository.save(user); // JpaRepository의 기본 메서드 호출
    }

    // 프로필 이미지 업로드
    public String uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // GCS에 업로드할 파일 이름 생성
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // GCS에 파일 업로드
        String fileUri = gcsService.uploadFile(fileName, file.getBytes());

        // Update user profileImageUri
        user.setProfileImageUri(fileUri);
        userRepository.save(user);

        return user.getProfileImageUri();
    }

    public static UserDTO mapToUserDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserName(user.getUserName());
        dto.setProfileImageUri(user.getProfileImageUri());
        dto.setStudyGroupId(user.getStudyGroup() != null ? user.getStudyGroup().getId() : null);
        dto.setStudyTime(user.getStudyTime());
        dto.setHelpGivenCount(user.getHelpGivenCount());
        dto.setHelpReceivedCount(user.getHelpReceivedCount());
        return dto;
    }

}
