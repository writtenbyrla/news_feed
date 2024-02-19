package com.example.news_feed.user.service;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;

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

}
