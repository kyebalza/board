package com.sparta.board.service;

import com.sparta.board.dto.TokenDto;
import com.sparta.board.dto.UserRequestDto;
import com.sparta.board.dto.UserResponseDto;
import com.sparta.board.entity.RefreshToken;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtProvider;
import com.sparta.board.repopsitory.UserRepository;
import com.sparta.board.repopsitory.RefreshTokenRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepositroy refreshTokenRepositroy;



    //회원가입
    public UserResponseDto registerUser(UserRequestDto userRequestDto)throws  IllegalAccessException{
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();
        String samepassword = userRequestDto.getSamepassword();

        Optional<User> findUserName = userRepository.findByUsername(username);//DB에서 username이 있는지 확인
        if(findUserName.isPresent()) {//username이 존재하면
            throw new IllegalAccessException("중복 닉네임 확인!");
        }
        if(!password.equals(samepassword)) {//password와 samepassword가 같지 않으면
            throw new IllegalAccessException("비밀번호가 서로 다릅니다!");
        }

        //password = passwordEncoder.encode(password);//밑에 것과 같은 것
        String encodingPassword = passwordEncoder.encode(password);

        UserRequestDto dto = UserRequestDto.builder()
                .username(username)
                .password(encodingPassword)
                .samepassword(encodingPassword)
                .build();

        User user = new User(dto);
        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto(user);

        return userResponseDto;
    }



    //로그인
    @Transactional
    public UserResponseDto login(UserRequestDto userRequestDto, HttpServletResponse response) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 userDetailsServiceImp 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtProvider.createTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepositroy.save(refreshToken);

        response.addHeader("Access-Token", tokenDto.getGrantType()+" "+tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());

        //suerReusetDto.getUsername()의 값과 동일한 username의 모든 정보 불러오기
        //User user = new User(userRequestDto);
        //User user 이렇게 쓸 수 있는 이유 : UsernameNotFoundException 로 null을 잡았기 때문에 optional<User> user을 빼 줄 수 있었음
        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow( ()-> new UsernameNotFoundException("유저를 찾을 수 없습니다"));

        UserResponseDto userResponseDto = new UserResponseDto(user);

        return userResponseDto;
    }


}
