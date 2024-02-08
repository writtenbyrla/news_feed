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
    @PatchMapping("/user/{userId}/toAdmin")
    public ResponseEntity<UserUpdateResponseDto> changeUserToAdmin(@PathVariable Long userId){
        adminUserService.changeUserRole(userId, UserRoleEnum.ADMIN);
        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), "유저 권한이 관리자로 변경되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 사용자 권한 변경(관리자 -> 유저)
    @PatchMapping("/user/{userId}/toUser")
    public ResponseEntity<UserUpdateResponseDto> changeUserToUser(@PathVariable Long userId){
        adminUserService.changeUserRole(userId, UserRoleEnum.USER);

        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), "관리자 권한이 일반 유저로 변경되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 사용자 권한 변경과 마찬가지로 api 분리할지 합칠지 고민
    // 강제 탈퇴
    @PatchMapping("/user/{userId}/unregister")
    public ResponseEntity<UserUpdateResponseDto> deleteUser(@PathVariable Long userId) {
        adminUserService.changeStatus(userId, "N");
        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), "유저 강제탈퇴 성공!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원 복구
    @PatchMapping("/user/{userId}/restore")
    public ResponseEntity<UserUpdateResponseDto> changeStatus(@PathVariable Long userId) {
        adminUserService.changeStatus(userId, "Y");
        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), "유저 복구 성공!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
