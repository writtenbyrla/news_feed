package com.example.news_feed.user.service;


import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageService {

    /*
     * 기본 프로필 수정
     * @param updateDto 프로필 수정 요청정보
     * @param userId 프로필 수정 요청자
     * @return 프로필 수정 결과
     */
    UserUpdateDto updateProfile(Long userId, UserUpdateDto updateDto);

    /*
     * 프로필 이미지 수정
     * @param file 프로필 이미지 수정 요청정보
     * @param userId 프로필 수정 요청자
     * @return 프로필 이미지 수정 결과
     */
    UserUpdateDto updateProfileImg(Long userId, MultipartFile file);

    /*
     * 패스워드 수정
     * @param pwdUpdateDto 패스워드 수정 요청정보
     * @param userId 패스워드 수정 요청자
     * @return 패스워드 수정 결과
     */
    PwdUpdateDto updatePwd(Long userId, PwdUpdateDto pwdUpdateDto);

    /*
     * 패스워드 히스토리 저장
     * @param pwd 패스워드 수정 요청정보
     * @param user 패스워드 수정 요청자
     * @return 패스워드 수정 결과
     */
    void saveHistory(User user, String pwd);

    /*
     * 회원정보 조회
     * @param userId 패스워드 수정 요청자
     * @return 회원 정보
     */
    UserDetailDto showUser(Long userId);

}