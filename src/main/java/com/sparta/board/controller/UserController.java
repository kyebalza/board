package com.sparta.board.controller;

import com.sparta.board.dto.UserRequestDto;
import com.sparta.board.dto.UserResponseDto;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //회원가입
    @PostMapping("/api/user/signup")
    public UserResponseDto signup(@RequestBody @Valid UserRequestDto userRequestDto) throws IllegalAccessException {
        return userService.registerUser(userRequestDto);
    }

    //로그인
    @PostMapping("/api/user/login")
    public UserResponseDto login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
        return userService.login(userRequestDto, response);
    }

}
