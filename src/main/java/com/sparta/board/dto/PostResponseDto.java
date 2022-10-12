package com.sparta.board.dto;

import com.sparta.board.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 글상세보기를 위한 Dto
public class PostResponseDto {

    private Long id;
    private String title;
    private String author;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto (Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.author = post.getUser().getUsername();//username을 불러오기 위함
        this.createAt = post.getCreateAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
