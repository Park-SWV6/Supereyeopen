package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long save(MemberDTO memberDTO){
        memberRepository.save(MemberEntity.toSaveEntity(memberDTO));
        MemberEntity memberEntity=MemberEntity.toSaveEntity(memberDTO);
        Long savedid=memberRepository.save(memberEntity).getId();
        return savedid;
    }
}
