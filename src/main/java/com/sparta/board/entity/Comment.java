package com.sparta.board.entity;

import com.sparta.board.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Comment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;        // 댓글 고유 id

    @ManyToOne
    @JoinColumn (nullable = false)
    private User user;  // 댓글 작성자

    @Column (nullable = false)
    private Long postId;    // 댓글이 달린 게시글 id

    @Column (nullable = false)
    private String comment;

    public Comment(CommentDto commentDto, User user) {
        this.postId = commentDto.getPostId();
        this.comment = commentDto.getComment();
        this.user = user;
    }

    public void update (CommentDto commentDto) {
        this.comment = commentDto.getComment();
    }
}
