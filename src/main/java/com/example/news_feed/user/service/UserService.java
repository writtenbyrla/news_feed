package com.example.news_feed.user.service;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.dto.response.UserInfoDto;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입
    @Transactional
    public User signup(SignupReqDto signupReqDto){


        // 유저네임 중복확인
        Optional<User> checkUsername = userRepository.findByName(signupReqDto.getUsername());

        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 이메일 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(signupReqDto.getEmail());
        if(checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 댓글 엔티티 생성
        User user = User.createUser(signupReqDto);

        // 댓글 엔티티를 DB로 저장
        User created = userRepository.save(user);

        // Dto로 변경하여 반환
        return userRepository.save(created);

    }

    // 로그인
    public User login(LoginReqDto loginReqDto) {
        User requestUser = loginReqDto.toEntity();

        // 사용자 확인
        User user = userRepository.findByEmail(requestUser.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        //
        if(!requestUser.getPwd().equals(user.getPwd())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    };

    // 기본 프로필 수정
    @Transactional
    public UserUpdateDto updateProfile(Long userId, UserUpdateDto updateDto) {

        updateDto.setUserId(userId);

        // 본인 닉네임 제외해서 중복확인
        Optional<User> checkUsername = userRepository.findByNameAndUserIdNot(updateDto.getUsername(), userId);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 수정 실패! 유저 정보가 없습니다."));

        // 댓글 수정
        target.patchProfile(updateDto);

        // DB
        User updated = userRepository.save(target);

        // 댓글 엔티티를 DTO로 변환해서 반환
        return UserUpdateDto.createUserDto(updated);
    }

    // 패스워드 수정
    @Transactional
    public PwdUpdateDto updatePwd(Long userId, PwdUpdateDto pwdUpdateDto) {

        pwdUpdateDto.setUserId(userId);

        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("비밀번호 수정 실패! 유저 정보가 없습니다."));

        // 입력된 현재 비밀번호와 기존 비밀번호 비교
        if (!pwdUpdateDto.getOldPwd().equals(target.getPwd())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 댓글 수정
        target.patchPwd(pwdUpdateDto);

        // 새로운 비밀번호로 업데이트
        User updated = userRepository.save(target);

        // 엔티티를 DTO로 변환해서 반환
        return PwdUpdateDto.createResponseDto(updated);
    }

}
