package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@Table(name="verification_code_table")
public class VerificationCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Date expiryDate;

    public VerificationCodeEntity() {}

    public VerificationCodeEntity(String email, String code, Date expiryDate){
        this.email = email;
        this.code = code;
        this.expiryDate = expiryDate;
    }

}
