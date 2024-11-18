package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.VerificationCodeEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationCodeRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //비밀번호 암호화
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //메일 전송
    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

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

    public void sendVerificationCode(String email) {
        // 인증번호 생성 (6자리 숫자)
        String verificationCode = String.valueOf((int)((Math.random() * 900000) + 100000));
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
}
