package com.sparta.board.service;

import com.sparta.board.dto.PostListDto;
import com.sparta.board.dto.PostRequestDto;
import com.sparta.board.dto.PostResponseDto;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.Post;
import com.sparta.board.entity.User;
import com.sparta.board.repopsitory.CommentRepository;
import com.sparta.board.repopsitory.PostRepository;
import com.sparta.board.repopsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;


//    //username을 이용해서  User 객체 만들기 및 유저정보 확인(보안)
//    private UserResponseDto getUser(String username) {
//        //exception 처리
////        Optional<User> mem = userRepository.findByUsername(username);
////        if(!mem.isPresent())
////            throw new IllegalArgumentException("사용자 정보가 없습니다!");
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
//
//        UserResponseDto userResponseDto = new UserResponseDto(user);
//        return userResponseDto;
//    }

    //username을 이용해서  User 객체 만들기 및 유저정보 확인(보안x)
    private User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        return user;
    }


    //글 작성
    public PostResponseDto create(PostRequestDto postRequestDto, String username) {
//        UserResponseDto userResponseDto = getUser(username);//보안
        User user = getUser(username);
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }


    //글 전체 조회
    public List<PostListDto> getPosts() {
        List<Post> list = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostListDto> postList = new ArrayList<>();
        for(Post post : list){
            PostListDto postlistDto = PostListDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .author(post.getUser().getUsername())
                    .createAt(post.getCreateAt())
                    .modifiedAt(post.getModifiedAt())
                    .build();
            postList.add(postlistDto);
        }
        return postList;
    }

    //글  조회
    public PostResponseDto getDetailPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }



    //글 수정
    public PostResponseDto update(Long id, PostRequestDto requestDto, String username) {
        User user = getUser(username);
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        if(!post.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        post.update(requestDto);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //글 삭제
    public void delete(Long id, String username) {
        User user = getUser(username);
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        if(!post.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        postRepository.deleteById(id);
        List<Comment> list = commentRepository.findAllByPostId(id);
        for(Comment comment : list) {
            commentRepository.deleteById(comment.getId());
        }

    }

}
