package com.example.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String writer;
    private String content;
    private String filename;
    private String filepath;
}
