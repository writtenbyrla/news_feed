package com.example.news_feed.user.service;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import com.example.news_feed.user.dto.response.UserDetailDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    /*
     * 회원가입
     * @param loginReqDto 회원가입 생성 요청정보
     * @return 회원가입 결과
     */
    User signup(SignupReqDto signupReqDto);

    /*
     * 로그인
     * @param loginReqDto 회원가입 생성 요청정보
     * @param HttpServletResponse 회원가입 요청에 대한 응답
     * @return 로그인 결과
     */
    LoginResponseDto login(LoginReqDto loginReqDto, HttpServletResponse response);

    /*
     * 유저 목록
     * @return 유저 전체 목록(탈퇴한 회원 제외)
     */
    Page<UserDetailDto> showAllUser(Pageable pageable);

    /*
     * 유저 검색
     * @param username 검색할 유저 이름
     * @return 유저 목록
     */
    Page<UserDetailDto> findByUsername(String username, Pageable pageable);
}
