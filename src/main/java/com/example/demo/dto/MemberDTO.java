package com.example.demo.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private int memberAge;

    public Object getMemberEmail() {
        return null;
    }

    public Object getMemberPassword() {
        return null;
    }

    public Object getMemberName() {
        return null;
    }

    public Object getMemberAge() {
        return null;
    }
}
