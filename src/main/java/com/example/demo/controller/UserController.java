package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
//@Controller
//@RequiredArgsConstructor
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        try {
            if (!userDTO.getUserPassword().equals(userDTO.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("패스워드가 일치하지 않습니다.");
            }
            Long userId = userService.registerUser(userDTO);
            return new ResponseEntity<>("사용자가 성공적으로 등록되었습니다. ID: " + userId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("사용자 등록 실패: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO loginDTO) {
        Optional<UserEntity> userOptional = userService.findByUserEmail(loginDTO.getUserEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 자격 증명");
        }

        UserEntity user = userOptional.get();
        if (!bCryptPasswordEncoder.matches(loginDTO.getUserPassword(), user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 자격 증명");
        }

        String token = jwtUtil.generateToken(user.getUserEmail());
        return ResponseEntity.ok(token); // 토큰 문자열만 반환
    }

    // 프론트 토큰 검즘 API
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header is missing or invalid");
        }
        try {
            // "Bearer " 부분 제거
            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);

            // 토큰 검증
            if(isValid) {
                String email = jwtUtil.extractEmail(token);
                return ResponseEntity.ok("Token is valid for email: " + email);
            } else {
                return ResponseEntity.status(401).body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error validating token: " + e.getMessage());
        }
    }
//    @GetMapping("/save-form")
//    public String saveForm(){
//        return "memberPages/save";
//    }
//    @PostMapping("/save")
//    public String save(@ModelAttribute UserDTO userDTO){
//        memberService.save(userDTO);
//        return "memberPages/login";
//    }
}
