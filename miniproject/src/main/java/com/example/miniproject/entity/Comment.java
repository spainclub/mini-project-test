package com.example.miniproject.entity;

import com.example.miniproject.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Post post;

    public Comment(CommentRequestDto commentRequestDto, User user) {
        this.writer = commentRequestDto.getWriter();
        this.content = commentRequestDto.getContent();
        this.user = user;
    }

    public void updateComment(CommentRequestDto commentRequestDto, User user) {
        this.writer = commentRequestDto.getWriter();
        this.content = commentRequestDto.getContent();
        this.user = user;
    }
}
