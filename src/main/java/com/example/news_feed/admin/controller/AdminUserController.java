package com.example.news_feed.admin.controller;

import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.admin.dto.response.UserUpdateResponseDto;
import com.example.news_feed.admin.service.serviceImpl.AdminUserServiceImpl;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminUserController {

    private final AdminUserServiceImpl adminUserServiceImpl;

    // 유저 전체 목록
    @GetMapping("/user")
    public ResponseEntity<Page<UserDetailDto>> showAllUser(@PageableDefault(value=10)
                                                               @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDetailDto> users = adminUserServiceImpl.showAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 사용자 권한 변경
    @PatchMapping("/user/{userId}/role")
    public ResponseEntity<UserUpdateResponseDto> changeUserToAdmin(@PathVariable Long userId,
                                                                   @RequestParam("role") UserRoleEnum role){

        String message = null;

        if (role.equals(UserRoleEnum.ADMIN)){
            adminUserServiceImpl.changeUserRole(userId, UserRoleEnum.ADMIN);
            message = "유저 권한이 관리자로 변경되었습니다.";
        } else if (role.equals(UserRoleEnum.USER)){
            adminUserServiceImpl.changeUserRole(userId, UserRoleEnum.USER);
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
            adminUserServiceImpl.changeStatus(userId, status);
            message = "유저 강제탈퇴 성공!";
        } else if(status.equals("Y")){
            adminUserServiceImpl.changeStatus(userId, status);
            message = "유저 복구 성공!";
        }

        UserUpdateResponseDto response = UserUpdateResponseDto.res(HttpStatus.OK.value(), message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
