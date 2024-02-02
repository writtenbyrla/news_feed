package com.example.news_feed.user.controller;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.dto.response.UserInfoDto;
import com.example.news_feed.user.dto.response.UserResponseDto;
import com.example.news_feed.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class MypageApiController {

    @Autowired
    private UserService userService;

    // 기본정보 수정
    @PatchMapping("/mypage/{userId}/profile")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable Long userId,
                                                         @RequestBody @Valid UserUpdateDto userUpdateDto,
                                                         BindingResult bindingResult){

        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            UserResponseDto response = UserResponseDto.res(HttpStatus.BAD_REQUEST.value(), errorMessages.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userService.updateProfile(userId, userUpdateDto);

        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "프로필 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    };

    // 패스워드 수정
    @PatchMapping("/mypage/{userId}")
    public ResponseEntity<UserResponseDto> updatePwd(@PathVariable Long userId,
                                                     @RequestBody @Valid PwdUpdateDto pwdUpdateDto,
                                                     BindingResult bindingResult){

        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            UserResponseDto response = UserResponseDto.res(HttpStatus.BAD_REQUEST.value(), errorMessages.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userService.updatePwd(userId, pwdUpdateDto);

        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "비밀번호 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };


}
