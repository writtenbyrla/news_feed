package com.example.news_feed.admin.service;

import com.example.news_feed.admin.dto.request.UserRoleUpdateDto;
import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.repository.AdminUserRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.dto.response.UserResponseDto;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

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
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("권한 수정 실패! 유저 정보가 없습니다."));

        // 같은 권한으로 변경하려 할 경우
        if (target.getRole() == newRole) {
            throw new IllegalArgumentException("이미 해당 권한을 가지고 있습니다.");
        }

        target.setRole(newRole);
        userRepository.save(target);
    }

    // 회원 상태 변경(탈퇴, 복구)
    public void changeStatus(Long userId, String status) {
        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("권한 수정 실패! 유저 정보가 없습니다."));

        if (target.getStatus().equals(status)) {
            throw new IllegalArgumentException("변경할 상태가 없습니다.");
        }

        target.setStatus(status);
        userRepository.save(target);

    }
}
