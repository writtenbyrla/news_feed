package com.example.news_feed.user.controller;

import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import com.example.news_feed.user.dto.response.UserResponseDto;
import com.example.news_feed.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserApiController {

    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupReqDto signupReqDto, BindingResult bindingResult){

        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            UserResponseDto response = UserResponseDto.res(HttpStatus.BAD_REQUEST.value(), errorMessages.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 회원가입 성공시
        userService.signup(signupReqDto);
        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "회원 가입 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    };

    // 로그인
    @PostMapping("/user/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginReqDto loginReqDto){

      // UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "로그인 완료");
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginReqDto));
    };



    }
