package com.example.news_feed.user.service.serviceImpl;

import com.example.news_feed.auth.redis.service.RedisService;
import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.security.jwt.TokenType;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import com.example.news_feed.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    @Transactional
    public User signup(SignupReqDto signupReqDto){
        String username = signupReqDto.getUsername();
        String pwd = bCryptPasswordEncoder.encode(signupReqDto.getPwd());
        String email = signupReqDto.getEmail();

        signupReqDto.setPwd(pwd);

        // 이메일 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()){
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL_FOUND);
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
                () ->  new UserException(UserErrorCode.USER_NOT_EXIST)
        );

        if (user.getStatus().equals("N")){
            throw new UserException(UserErrorCode.USER_DELETED);
        }

        if(!bCryptPasswordEncoder.matches(pwd, user.getPwd())){
            throw new UserException(UserErrorCode.MISMATCH_PASSWORD);
        }

        String username = user.getUsername();
        UserRoleEnum role = user.getRole();

        String accessToken = jwtTokenProvider.createToken(email, username, role, TokenType.ACCESS);
        String refreshToken = jwtTokenProvider.createToken(email, username, role, TokenType.REFRESH);

        return new LoginResponseDto(accessToken, refreshToken, email, username, role);
    }

    // 유저 전체 목록(탈퇴 회원 제외)
    @Override
    public Page<UserDetailDto> showAllUser(Pageable pageable) {
        Page<User> users = userRepository.showAllUser(pageable);

        return new PageImpl<>(
                users.getContent().stream()
                        .map(UserDetailDto::createUserDetailDto)
                        .collect(Collectors.toList()),
                pageable,
                users.getTotalElements()
        );
    }

    // 유저 검색
    @Override
    public Page<UserDetailDto> findByUsername(String username, Pageable pageable) {
        Page<User> filteredUser = userRepository.findByUsername(username, pageable);
        if(filteredUser.isEmpty()){
            throw new UserException(UserErrorCode.USER_NOT_EXIST);
        }
        return new PageImpl<>(
                filteredUser.getContent().stream()
                        .map(UserDetailDto::createUserDetailDto)
                        .collect(Collectors.toList()),
                pageable,
                filteredUser.getTotalElements()
        );


    }
}
