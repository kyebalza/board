package com.sparta.board.service;

import com.sparta.board.dto.CommentDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.repopsitory.CommentRepository;
import com.sparta.board.repopsitory.PostRepository;
import com.sparta.board.repopsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    //유저 이름 찾기
    private User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        return user;
    }

    //게시글 번호 찾기
    private void extracted(CommentDto commentDto) {
        postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
    }

    //댓글 생성
    public CommentResponseDto createComment(CommentDto commentDto, String username) {
        User member = getUser(username);
        extracted(commentDto);
        Comment comment = new Comment(commentDto, member);
        commentRepository.save(comment);
        return CommentResponseDto.builder()
                .id(comment.getId())
//                .author((comment.getUser().getUsername()))
                .author(username)
                .comment(comment.getComment())
                .createAt(comment.getCreateAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentDto commentDto, String username) {
        User member = getUser(username);
        extracted(commentDto);
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if(!member.getUsername().equals(comment.getUser().getUsername()))
            throw new IllegalArgumentException("댓글 작성자가 다릅니다.");
        comment.update(commentDto);
        return CommentResponseDto.builder()
                .id(comment.getId())
                .author((comment.getUser().getUsername()))
                .comment(comment.getComment())
                .createAt(comment.getCreateAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }


    // 댓글 삭제
    public Long deleteComment(Long id, String username)  {
        User user = getUser(username);
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if(!user.getUsername().equals(comment.getUser().getUsername()))
            throw new IllegalArgumentException("댓글 작성자가 다릅니다.");
        commentRepository.deleteById(id);
        return id;
    }

    // 댓글 목록 조회
    public List<CommentResponseDto> getCommentList(Long id) {
        List<Comment> list = commentRepository.findAllByPostId(id);
        List<CommentResponseDto> clist = new ArrayList<>();
        for (Comment c : list) {
            CommentResponseDto commentDto = CommentResponseDto.builder()
                    .id(c.getId())
                    .author(c.getUser().getUsername())
                    .comment(c.getComment())
                    .createAt(c.getCreateAt())
                    .modifiedAt(c.getModifiedAt())
                    .build();
            clist.add(commentDto);
        }

        return clist;
    }
}
