package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class AutoController {
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user){
//        log.info("사용자 로그인 시도:{}", user.getUsername());
//
//        try{
//            UserDetails userDetails= customUserDetailsService.loadUserByUsername(user.getUsername());
//
//            boolean passwordMatch=passwordEncoder.matcher(user.getPassword(), userDetails.getpassword());
//            log.info("비밀번호 일치 여부:{}".passwordMatch);
//
//            if(!passwordMatch){
//                throw new BadCredentialsException("자격 증명이 올바르지 않습니다.");
//            }
//
//            Authentication authentication=authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
//            );
//            String token=jwtUtil.generateToken(authentication.getName());
//            log.info("사용자가 성공적으로 로그인했습니다: {}", user.getUsername());
//            return ResponseEntity.ok(Collections.singletonMap("token", token));
//        }  catch(BadCredentialsException e){
//            log.error("사용자 {}의 로그인 실패: {}", user.getUsername(), e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 잘못된 자격 증명입니다.");
//        } catch(Exception e){
//            log.error("사용자 {}의 로그인 실패: {}", user.getUsername(), e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
//        }
//    }
}
