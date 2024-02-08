package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.service.AdminUserService;
import com.example.news_feed.user.dto.response.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    // 유저 전체 목록
    @GetMapping("/user")
    public ResponseEntity<List<UserDetailDto>> showAllUser(){
        List<UserDetailDto> users = adminUserService.showAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 사용자 권한 변경


    // 강제 탈퇴
}