package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.dto.response.UserUpdateResponseDto;
import com.example.news_feed.admin.service.AdminUserService;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 유저 전체 목록
    @GetMapping("/user")
    public ResponseEntity<List<UserDetailDto>> showAllUser(){
        List<UserDetailDto> users = adminUserService.showAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 사용자 권한 변경
    @PatchMapping("/user/{userId}/role")
    public ResponseEntity<UserUpdateResponseDto> changeUserToAdmin(@PathVariable Long userId,
                                                                   @RequestParam("role") UserRoleEnum role){

        String message = null;

        if (role.equals(UserRoleEnum.ADMIN)){
            adminUserService.changeUserRole(userId, UserRoleEnum.ADMIN);
            message = "유저 권한이 관리자로 변경되었습니다.";
        } else if (role.equals(UserRoleEnum.USER)){
            adminUserService.changeUserRole(userId, UserRoleEnum.USER);
            message = "유저 권한이 사용자로 변경되었습니다.";
        }

        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 강제 탈퇴, 복구
    @PatchMapping("/user/{userId}/status")
    public ResponseEntity<UserUpdateResponseDto> deleteUser(@PathVariable Long userId,
                                                            @RequestParam("status") String status) {

        String message=null;

        if (status.equals("N")){
            adminUserService.changeStatus(userId, status);
            message = "유저 강제탈퇴 성공!";
        } else if(status.equals("Y")){
            adminUserService.changeStatus(userId, status);
            message = "유저 복구 성공!";
        }

        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
