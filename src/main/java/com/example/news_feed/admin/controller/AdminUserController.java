package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.dto.response.UserUpdateResponseDto;
import com.example.news_feed.admin.service.AdminUserService;
import com.example.news_feed.user.domain.UserRoleEnum;
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

    // 엔드포인트에 전달되는 권한 키워드 확인하여 역할 변경하는 하나의 api로 합칠지
    // 각각의 api로 나누는게 좋은지 잘 모르겠음..(service단 로직은 하나로 사용)
    // 사용자 권한 변경(유저 -> 관리자)
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


    // 사용자 권한 변경과 마찬가지로 api 분리할지 합칠지 고민
    // 강제 탈퇴
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
