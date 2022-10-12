package com.sparta.board.dto;

import com.sparta.board.entity.User;
import lombok.Getter;

@Getter
public class CommentDto {
    private User user;
    private String comment;
    private Long postId;
}
