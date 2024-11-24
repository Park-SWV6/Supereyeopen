package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HelpRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String date;
    private int comments;
    private List<String> uri;
    private Long userId; // User ID
    private String userName; // User Name
}
