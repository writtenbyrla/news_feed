package com.example.news_feed.user.controller;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.auth.dto.response.LogoutResponseDto;
import com.example.news_feed.auth.redis.service.LogoutService;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.response.LoginResponseDto;
import com.example.news_feed.user.dto.response.UserResponseDto;
import com.example.news_feed.user.service.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserServiceImpl userServiceImpl;
    private final LogoutService logoutService;

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupReqDto signupReqDto, BindingResult bindingResult){

        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            throw new HttpException(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }

        // 회원가입 성공시
        User user = userServiceImpl.signup(signupReqDto);
        UserResponseDto response = UserResponseDto.res(HttpStatus.CREATED.value(), "회원 가입 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    };

    // 로그인
    @PostMapping("/user/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body(userServiceImpl.login(loginReqDto, response));
    };

    // 로그아웃
    @PostMapping("/user/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request,
                                                    @AuthenticationPrincipal UserDetailsImpl userdetails){
        logoutService.logout(request, userdetails.getEmail());
        LogoutResponseDto response = LogoutResponseDto.res(HttpStatus.OK.value(), "로그아웃 완료!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 목록(탈퇴한 회원 제외)
    @GetMapping("/user/list")
    public ResponseEntity<Page<UserDetailDto>> showAll(@PageableDefault(value=10)
                                                           @SortDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDetailDto> users = userServiceImpl.showAllUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 유저 검색
    @GetMapping("/user/search")
    public ResponseEntity<Page<UserDetailDto>> findByUsername(@RequestParam("username") String username,
                                                             @PageableDefault(value=10)
                                                             @SortDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDetailDto> users = userServiceImpl.findByUsername(username, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


}
