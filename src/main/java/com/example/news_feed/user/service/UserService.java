package com.example.news_feed.user.service;

import com.example.news_feed.security.jwt.JwtTokenProvider;
import com.example.news_feed.security.jwt.TokenType;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    // 회원가입
    @Transactional
    public User signup(SignupReqDto signupReqDto){
        String username = signupReqDto.getUsername();
        String pwd = bCryptPasswordEncoder.encode(signupReqDto.getPwd());
        String email = signupReqDto.getEmail();

        signupReqDto.setPwd(pwd);
        signupReqDto.setCreatedAt(new Date());

        // 유저네임 중복확인
        Optional<User> checkUsername = userRepository.findByName(username);
        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 이메일 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인(admin 토큰이 있을 경우에만 admin으로 회원가입)
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupReqDto.isAdmin()) {
            if(!ADMIN_TOKEN.equals(signupReqDto.getAdminToken())){
                throw new IllegalArgumentException("관리자 암호가 틀립니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        signupReqDto.setRole(role);

        // 유저 엔티티 생성
        User user = User.createUser(signupReqDto);

        // 유저 엔티티를 DB로 저장
        User created = userRepository.save(user);

        // Dto로 변경하여 반환
        return userRepository.save(created);

    }

    // 로그인
    public LoginResponseDto login(LoginReqDto loginReqDto) {

        String email = loginReqDto.getEmail();
        String pwd = loginReqDto.getPwd();

        // 사용자 확인(email)
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        String username = user.getUsername();
        UserRoleEnum role = user.getRole();
        if(!bCryptPasswordEncoder.matches(pwd, user.getPwd())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(email, username, role, TokenType.ACCESS);
        String refreshToken = jwtTokenProvider.createToken(email, username, role, TokenType.REFRESH);

        return new LoginResponseDto(accessToken, refreshToken, email, username, role);
    };

    // 기본 프로필 수정
    @Transactional
    public UserUpdateDto updateProfile(Long userId, UserUpdateDto updateDto) {

        updateDto.setUserId(userId);
        updateDto.setUpdatedAt(new Date());

        // 본인 닉네임 제외해서 중복확인
        Optional<User> checkUsername = userRepository.findByNameAndUserIdNot(updateDto.getUsername(), userId);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 수정 실패! 유저 정보가 없습니다."));

        // 프로필 수정
        target.patchProfile(updateDto);

        // DB
        User updated = userRepository.save(target);

        // 엔티티를 DTO로 변환해서 반환
        return UserUpdateDto.createUserDto(updated);
    }

    // 패스워드 수정
    @Transactional
    public PwdUpdateDto updatePwd(Long userId, PwdUpdateDto pwdUpdateDto) {

        pwdUpdateDto.setUserId(userId);
        pwdUpdateDto.setUpdatedAt(new Date());
        String oldPwd = pwdUpdateDto.getOldPwd();

        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("비밀번호 수정 실패! 유저 정보가 없습니다."));

        // 입력된 현재 비밀번호와 기존 비밀번호 비교
        if (!bCryptPasswordEncoder.matches(oldPwd, target.getPwd())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 암호 수정
        pwdUpdateDto.setNewPwd(bCryptPasswordEncoder.encode(pwdUpdateDto.getNewPwd()));
        target.patchPwd(pwdUpdateDto);

        // 새로운 비밀번호로 업데이트
        User updated = userRepository.save(target);

        // 엔티티를 DTO로 변환해서 반환
        return PwdUpdateDto.createResponseDto(updated);
    }

}
