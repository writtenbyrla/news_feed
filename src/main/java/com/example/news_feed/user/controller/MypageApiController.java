package com.example.news_feed.user.controller;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.dto.response.UserResponseDto;
import com.example.news_feed.user.service.serviceImpl.MyPageServiceImpl;
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
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/myPage/*")
@Slf4j
public class MypageApiController {

    private final MyPageServiceImpl mypageServiceImpl;

    // 프로필 수정
    @PatchMapping("/{userId}/profile")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable Long userId,
                                                         @RequestBody @Valid UserUpdateDto userUpdateDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         BindingResult bindingResult){

        // 조건에 맞지 않으면 에러 메시지 출력
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new HttpException(errorMessages.toString(), HttpStatus.BAD_REQUEST);

        }

        userUpdateDto.setUserId(userDetails.getId());
        mypageServiceImpl.updateProfile(userId, userUpdateDto);
        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "프로필 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 프로필 이미지 수정
    @PatchMapping("/{userId}/profileImg")
    public ResponseEntity<UserResponseDto> updateProfileImg(@PathVariable Long userId,
                                                             @RequestPart(value = "file") MultipartFile file,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails){

        mypageServiceImpl.updateProfileImg(userDetails, userId, file);
        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "프로필 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };


    // 프로필 이미지 받아오기
    @GetMapping("/{userId}/profileImg")
    public ResponseEntity<String> profileImg(@PathVariable Long userId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){

        UserDetailDto user = mypageServiceImpl.findById(userDetails, userId);
        return ResponseEntity.status(HttpStatus.OK).body(user.getProfileImg());
    }

    // 패스워드 수정
    @PatchMapping("/{userId}/pwd")
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

            throw new HttpException(errorMessages.toString(), HttpStatus.BAD_REQUEST);

        }

        mypageServiceImpl.updatePwd(userDetails, userId, pwdUpdateDto);

        UserResponseDto response = UserResponseDto.res(HttpStatus.OK.value(), "비밀번호 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 내 정보 조회
    @GetMapping("/{userId}/info")
    public ResponseEntity<UserDetailDto> myInfo(@PathVariable Long userId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(mypageServiceImpl.findById(userDetails, userId));
    }

    // 내가 쓴 게시글 목록
    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostDetailDto>> showMyPost(@PathVariable Long userId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PageableDefault(value=10)
                                                              @SortDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDetailDto> posts = mypageServiceImpl.showMyPost(userId, userDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    // 내가 쓴 댓글 목록
    @GetMapping("/{userId}/comments")
    public ResponseEntity<Page<CommentDetailDto>> showMyComment(@PathVariable Long userId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PageableDefault(value=10)
                                                                    @SortDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentDetailDto> comments = mypageServiceImpl.showMyComment(userId, userDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    // 내가 팔로우한 사람
    @GetMapping("/{userId}/followings")
    public ResponseEntity<Page<UserDetailDto>> showMyFollowings(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PageableDefault(value = 10)
                                                                    @SortDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDetailDto> users = mypageServiceImpl.showMyFollowings(userId, userDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // 나를 팔로우한 사람
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<UserDetailDto>> showMyFollowers(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PageableDefault(value = 10)
                                                                @SortDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDetailDto> users = mypageServiceImpl.showMyFollowers(userId, userDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

}
