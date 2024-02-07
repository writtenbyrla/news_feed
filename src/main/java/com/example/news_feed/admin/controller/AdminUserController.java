package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
