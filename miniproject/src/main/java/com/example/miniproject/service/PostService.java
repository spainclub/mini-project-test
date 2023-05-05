package com.example.miniproject.service;

import com.example.miniproject.dto.CommentResponseDto;
import com.example.miniproject.dto.PostRequestDto;
import com.example.miniproject.dto.PostResponseDto;
import com.example.miniproject.dto.ResponseDto;
import com.example.miniproject.entity.Comment;
import com.example.miniproject.entity.Post;
import com.example.miniproject.entity.User;
import com.example.miniproject.entity.UserRoleEnum;
import com.example.miniproject.repository.CommentRepository;
import com.example.miniproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 게시글 전체 조회
    public List<PostResponseDto> getPostList(){
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        return postList.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    // 게시글 작성
    public PostResponseDto writePost(PostRequestDto postRequestDto, User user) {
//        // 파일 저장 경로
//        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//MultipartFile file,
//        UUID uuid = UUID.randomUUID();  // 식별자 랜덤 생성
//
//        String fileName = uuid + "_" + file.getOriginalFilename();  // 랜덤식별자_본래파일명
//
//        File saveFile = new File(projectPath, fileName);
//
//        postRequestDto.setFilename(fileName);
//        postRequestDto.setFilepath("/files/" + fileName);  // 저장된 파일경로와 파일 이름
//
//        file.transferTo(saveFile);

        // 게시글 저장
        Post post = postRepository.saveAndFlush(new Post(postRequestDto,user));
        return new PostResponseDto(post);
    }

    //게시글 하나 조회
    @Transactional
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        List<Comment> commentList = commentRepository.findAll();
        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            if (Objects.equals(comment.getPost().getId(), postId)) {
                comments.add(new CommentResponseDto(comment));
            }
        }
        return new PostResponseDto(post);
    }

    // 게시물 수정
    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
        if (user.getRole() != UserRoleEnum.ADMIN && !StringUtils.equals(post.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다." + HttpStatus.BAD_REQUEST);
        }
        post.updatePost(postRequestDto);
        return new PostResponseDto(post);
    }

    // 게시물 삭제
    @Transactional
    public ResponseDto deletePost(Long postId, User user){
        Post post = new Post();
        if (user.getRole() != UserRoleEnum.ADMIN && !StringUtils.equals(post.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다." + HttpStatus.BAD_REQUEST);
        }
        postRepository.deleteById(postId);
        return new ResponseDto("삭제 완료");
    }

}


/*
UUID : 유일한 식별자 생성

업로드된 파일명의 중복 방지하기 위해 파일명 변경 할 때 사용
첨부파일 다운로드시 다른 파일을 예측하여 다운로드 하는 것을 방지하는데 사용
일련 번호 대신 유추하기 힘든 식별자를 사용하여 다른 컨텐츠의 임의 접근을 방지하는데 사용

 */