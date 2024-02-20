package com.example.news_feed.user.service;


import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    UserUpdateDto updateProfileImg(UserDetailsImpl userDetails, Long userId, MultipartFile file);

    /*
     * 패스워드 수정
     * @param pwdUpdateDto 패스워드 수정 요청정보
     * @param userId 패스워드 수정 요청자
     * @return 패스워드 수정 결과
     */
    PwdUpdateDto updatePwd(UserDetailsImpl userDetails, Long userId, PwdUpdateDto pwdUpdateDto);

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
    UserDetailDto findById(UserDetailsImpl userDetails, Long userId);


    Page<PostDetailDto> showMyPost(Long userId, UserDetailsImpl userDetails, Pageable pageable);

    Page<CommentDetailDto> showMyComment(Long userId, UserDetailsImpl userDetails, Pageable pageable);

    Page<UserDetailDto> showMyFollowings(Long userId, UserDetailsImpl userDetails, Pageable pageable);

    Page<UserDetailDto> showMyFollowers(Long userId, UserDetailsImpl userDetails, Pageable pageable);
}