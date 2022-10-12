package com.sparta.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String author;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


}
