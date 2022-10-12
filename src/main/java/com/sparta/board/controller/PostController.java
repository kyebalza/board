package com.sparta.board.controller;

import com.sparta.board.dto.PostListDto;
import com.sparta.board.dto.PostRequestDto;
import com.sparta.board.dto.PostResponseDto;
import com.sparta.board.service.PostService;
import com.sparta.board.security.UserDetailImp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //글 작성
    @PostMapping("/api/auth/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailImp userDetail)  {
        return postService.create(postRequestDto, userDetail.getUsername());
    }
    //글 전체 조회
    @GetMapping("/api/posts")
    public List<PostListDto> getPosts() {
        return postService.getPosts();
    }

    //글  조회
    @GetMapping("/api/post/{id}")
    public PostResponseDto getDetailPosts(@PathVariable Long id) {
        return postService.getDetailPost(id);
    }

    //글 수정
    @PutMapping("/api/auth/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailImp userDetail)  {
        return postService.update(id, requestDto, userDetail.getUsername());
    }

    //글 삭제
    @DeleteMapping("/api/auth/post/{id}")
    public Long deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailImp userDetail) {
        postService.delete(id, userDetail.getUsername());
        return id;
    }
}
