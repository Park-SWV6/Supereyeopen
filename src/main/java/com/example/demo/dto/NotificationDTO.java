package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationDTO {
    private Long id;
    private Long receiverId;
    private String title;
    private String message;
    private boolean IsRead;
}
