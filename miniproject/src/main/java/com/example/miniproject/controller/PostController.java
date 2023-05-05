package com.example.miniproject.controller;

import com.example.miniproject.dto.PostRequestDto;
import com.example.miniproject.dto.PostResponseDto;
import com.example.miniproject.dto.ResponseDto;
import com.example.miniproject.security.UserDetailsImpl;
import com.example.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시물 전체 보기
    // 토큰 필요 없음
    // 메인 페이지 , 게시물 전체 불러오기
    @GetMapping("/api")
    public ModelAndView home(Model model) {
        model.addAttribute("post", postService.getPostList());

        return new ModelAndView("index");
    }

    // 게시물 작성
    @PostMapping("/api/write/post")
    public PostResponseDto writePost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.writePost(postRequestDto, userDetails.getUser());
    }

    // 게시물 하나 보기
    @GetMapping("/api/select/post/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    // 게시물 수정
    @PutMapping("/api/update/post/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postRequestDto, userDetails.getUser());
    }

    // 게시물 삭제
    // 게시물 삭제 후 메인 페이지로 반환 해야함
    @DeleteMapping("/api/delete/post/{postId}")
    public ResponseDto deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(postId, userDetails.getUser());
    }

}
