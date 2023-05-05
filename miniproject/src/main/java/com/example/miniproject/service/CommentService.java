package com.example.miniproject.service;
import com.example.miniproject.dto.CommentResponseDto;
import com.example.miniproject.entity.User;
import com.example.miniproject.dto.CommentRequestDto;
import com.example.miniproject.entity.Comment;
import com.example.miniproject.entity.UserRoleEnum;
import com.example.miniproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    //댓글 작성
    @Transactional
    public CommentResponseDto writeComment(CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.saveAndFlush(new Comment(commentRequestDto, user));
        return new CommentResponseDto(comment);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        if (user.getRole() != UserRoleEnum.ADMIN && !StringUtils.equals(comment.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다." + HttpStatus.BAD_REQUEST);
        }
        comment.updateComment(commentRequestDto, user);
        return new CommentResponseDto(comment);
    }

    //댓글 삭제
    @Transactional
    public String deleteComment(Long commentId, User user) {
        Comment comment = new Comment();
        if (user.getRole() != UserRoleEnum.ADMIN && !StringUtils.equals(comment.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다." + HttpStatus.BAD_REQUEST);
        }
        commentRepository.deleteById(commentId);
        return "삭제 완료";
    }


}
