package com.sparta.board.dto;

import com.sparta.board.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
// 글목록가져오기위한 Dto
public class PostListDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    //Builder 없을 경우
//    private PostListDto(Post post){
//        this.id= post.getId();
//        this.title= post.getTitle();
//        this.author= post.getUser().getUsername();
//        this.createAt= post.getCreateAt();
//        this.modifiedAt= post.getModifiedAt();
//
//    }
}
