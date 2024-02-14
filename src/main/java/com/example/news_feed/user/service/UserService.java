package com.example.news_feed.user.service;

import com.example.news_feed.auth.dto.response.LogoutResponseDto;
import com.example.news_feed.auth.redis.service.LogoutService;
import com.example.news_feed.auth.redis.service.RedisService;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.security.jwt.TokenType;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisService redisService;

    // 회원가입
    @Transactional
    public User signup(SignupReqDto signupReqDto){
        String username = signupReqDto.getUsername();
        String pwd = bCryptPasswordEncoder.encode(signupReqDto.getPwd());
        String email = signupReqDto.getEmail();

        signupReqDto.setPwd(pwd);

        // 유저네임 중복확인
        Optional<User> checkUsername = userRepository.findByName(username);
        if(checkUsername.isPresent()){
            throw new HttpException(false, "중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // 이메일 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()) {
            throw new HttpException(false, "중복된 Email 입니다.", HttpStatus.BAD_REQUEST);
        }

        // 유저 엔티티 생성
        User user = User.createUser(signupReqDto);

        // 유저 엔티티를 DB로 저장
        User created = userRepository.save(user);

        // Dto로 변경하여 반환
        return userRepository.save(created);
    }

    // 로그인
    public LoginResponseDto login(LoginReqDto loginReqDto, HttpServletResponse response) {

        String email = loginReqDto.getEmail();
        String pwd = loginReqDto.getPwd();

        // 사용자 확인(email)
        User user = userRepository.findByEmail(email).orElseThrow(
                () ->  new HttpException(false, "등록된 사용자가 없습니다.", HttpStatus.BAD_REQUEST)
        );

        String username = user.getUsername();
        UserRoleEnum role = user.getRole();

        log.info(user.getPwd());
        if(!bCryptPasswordEncoder.matches(pwd, user.getPwd())){
            throw new HttpException(false, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String accessToken = jwtTokenProvider.createToken(email, username, role, TokenType.ACCESS);
        String refreshToken = jwtTokenProvider.createToken(email, username, role, TokenType.REFRESH);

        Long expiresTime = jwtTokenProvider.getExpiredTime(refreshToken, TokenType.REFRESH);
        redisService.setValues("RefreshToken:" + user.getEmail(), refreshToken, expiresTime, TimeUnit.MILLISECONDS);

        // 토큰을 헤더에 넣어서 클라이언트에게 전달
        jwtTokenProvider.accessTokenSetHeader(accessToken, response);
        jwtTokenProvider.refreshTokenSetHeader(refreshToken, response);

        return new LoginResponseDto(accessToken, refreshToken, email, username, role);
    };


}
