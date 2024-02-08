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

    // 회원 강제 탈퇴
}
