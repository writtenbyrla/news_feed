package com.example.news_feed.admin.service;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.user.domain.UserRoleEnum;

import java.util.List;

public interface AdminUserService {
    /*
     * 유저 목록
     * @return 유저 전체 목록
     */
    List<UserDetailDto> showAll();

    /*
     * 유저 권한 변경
     * @param newRole 변경하려는 권한 정보
     * @param userId 대상 유저 정보
     * @return 유저 권한 변경 결과
     */
    void changeUserRole(Long userId, UserRoleEnum newRole);

    /*
     * 유저 상태 변경(탈퇴)
     * @param status 변경하려는 상태 정보
     * @param userId 대상 유저 정보
     * @return 유저 상태 변경 결과
     */
    void changeStatus(Long userId, String status);
}
