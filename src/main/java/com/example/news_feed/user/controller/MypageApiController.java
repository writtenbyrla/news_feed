package com.example.news_feed.user.controller;
import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.dto.response.UserResponseDto;
import com.example.news_feed.user.serviceImpl.MyPageServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MypageApiController {

    private final MyPageServiceImpl mypageServiceImpl;

    // 프로필 수정
    @PatchMapping("/myPage/{userId}/profile")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable Long userId,
                                                         @RequestBody @Valid UserUpdateDto userUpdateDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         BindingResult bindingResult){
        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            UserResponseDto response = UserResponseDto.res(HttpStatus.BAD_REQUEST.value(), errorMessages.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        userUpdateDto.setUserId(userDetails.getId());
        mypageServiceImpl.updateProfile(userId, userUpdateDto);
        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "프로필 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 프로필 이미지 수정
    @PatchMapping("/myPage/{userId}/profileImg")
    public ResponseEntity<UserResponseDto> updateProfileImg(@PathVariable Long userId,
                                                             @RequestPart(value = "file") MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails){

        mypageServiceImpl.updateProfileImg(userDetails, userId, file);
        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "프로필 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };


    // 프로필 이미지 받아오기
    @GetMapping("/myPage/{userId}/profileImg")
    public ResponseEntity<String> profileImg(@PathVariable Long userId){
        UserDetailDto user = mypageServiceImpl.showUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user.getProfileImg());
    }

    // 패스워드 수정
    @PatchMapping("/myPage/{userId}")
    public ResponseEntity<UserResponseDto> updatePwd(@PathVariable Long userId,
                                                     @RequestBody @Valid PwdUpdateDto pwdUpdateDto,
                                                     BindingResult bindingResult,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){

        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            UserResponseDto response = UserResponseDto.res(HttpStatus.BAD_REQUEST.value(), errorMessages.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        mypageServiceImpl.updatePwd(userDetails, userId, pwdUpdateDto);

        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "비밀번호 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 회원정보 조회

    // 내가 쓴 게시글 조회

    // 내가 쓴 댓글 조회

    // 내가 팔로우한 사람

    // 나를 팔로우한 사람

}
