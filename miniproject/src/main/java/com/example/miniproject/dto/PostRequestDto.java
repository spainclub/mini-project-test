package com.example.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String contents;
    private String filename;
    private String filepath;
}
