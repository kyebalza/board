package com.sparta.board.dto;

import com.sparta.board.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
//@Builder//생성자 자동 생성
public class UserResponseDto {
    private Long id;
    private String username;
//    private String password;
//    private String samepassword;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public  UserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.createAt = user.getCreateAt();
        this.modifiedAt = user.getModifiedAt();
    }


}
