package com.example.news_feed.admin.serviceImpl;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.exception.AdminErrorCode;
import com.example.news_feed.admin.exception.AdminException;
import com.example.news_feed.admin.repository.AdminUserRepository;
import com.example.news_feed.admin.service.AdminUserService;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private UserRepository userRepository;

    // 유저 전체 목록(탈퇴회원 포함)
    public List<UserDetailDto> showAll() {

        return adminUserRepository.findAll()
                .stream()
                .map(UserDetailDto::createUserDetailDto)
                .collect(Collectors.toList());
    }

    // 유저 권한 변경
    public void changeUserRole(Long userId, UserRoleEnum newRole) {
        // 기존 유저정보 조회 및 예외 처리
        User target = checkUser(userId);

        // 같은 권한으로 변경하려 할 경우
        if (target.getRole().equals(newRole)) {
            throw new AdminException(AdminErrorCode.ALREADY_HAS_ROLE);
        }

        target.setRole(newRole);
        userRepository.save(target);
    }

    // 회원 상태 변경(탈퇴, 복구)
    public void changeStatus(Long userId, String status) {
        // 기존 유저정보 조회 및 예외 처리
        User target = checkUser(userId);

        if (target.getStatus().equals(status)) {
            throw new AdminException(AdminErrorCode.ALREADY_HAS_STATUS);
        }
        target.setStatus(status);
        userRepository.save(target);
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }
}
