package com.example.news_feed.user.service.serviceImpl;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.comment.exception.CommentErrorCode;
import com.example.news_feed.comment.exception.CommentException;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.follow.domain.Follow;
import com.example.news_feed.follow.exception.FollowErrorCode;
import com.example.news_feed.follow.exception.FollowException;
import com.example.news_feed.follow.repository.FollowRepository;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.common.aws.FileUploadService;
import com.example.news_feed.user.domain.PwdHistory;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.AuthHistoryRepository;
import com.example.news_feed.user.repository.UserRepository;
import com.example.news_feed.user.service.MyPageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final AuthHistoryRepository historyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileUploadService fileUploadService;

    // 기본 프로필 수정
    @Transactional
    public UserUpdateDto updateProfile(Long userId, UserUpdateDto updateDto) {

        // 본인 닉네임 제외해서 중복확인
        Optional<User> checkUsername = userRepository.findByNameAndUserIdNot(updateDto.getUsername(), userId);
        if(checkUsername.isPresent()){
            throw new UserException(UserErrorCode.DUPLICATE_USERNAME_FOUND);
        }

        // 기존 유저정보 조회 및 예외 처리
        User target = checkUser(userId);

        // 본인 여부 확인
        isValidUser(target.getUserId(), updateDto.getUserId());

        // 프로필 수정
        updateDto.setUserId(userId);
        target.patchProfile(updateDto);

        // DB
        User updated = userRepository.save(target);

        // 엔티티를 DTO로 변환해서 반환
        return UserUpdateDto.createUserDto(updated);
    }

    // 프로필 이미지 수정
    @Transactional
    public UserUpdateDto updateProfileImg(UserDetailsImpl userDetails, Long userId, MultipartFile file) {

        // 기존 유저정보 조회 및 예외 처리
        User user = checkUser(userId);

        // 본인 여부 확인
        isValidUser(user.getUserId(), userDetails.getId());

        // s3에 파일 업로드
        String profileUrl = fileUploadService.uploadProfile(file);

        // 프로필 수정
        user.setProfileImg(profileUrl);

        // DB
        User updated = userRepository.save(user);

        // 엔티티를 DTO로 변환해서 반환
        return UserUpdateDto.createUserDto(updated);
    }

    // 패스워드 수정
    @Transactional
    public PwdUpdateDto updatePwd(UserDetailsImpl userDetails, Long userId, PwdUpdateDto pwdUpdateDto) {

        pwdUpdateDto.setUserId(userId);
        String oldPwd = pwdUpdateDto.getOldPwd(); // 현재 비밀번호 입력값

        // 기존 유저정보 조회 및 예외 처리
        User target = checkUser(userId);

        // 본인 여부 확인
        isValidUser(target.getUserId(), userDetails.getId());

        // 기존 등록된 비밀번호와 현재 비밀번호 입력값이 일치하는지 확인
        String currnetPwd = target.getPwd();

        if (!bCryptPasswordEncoder.matches(oldPwd, currnetPwd)) {
            throw new UserException(UserErrorCode.MISMATCH_PASSWORD);
        }

        // 1.최근 사용한 패스워드 3개 일치여부 확인 후
        // 2. 히스토리에 현재 패스워드 저장
        // 3. 새로운 비밀번호로 업데이트

        // 1. pwds 리스트의 각 요소의 oldPwd 필드와 입력받은 새로운 비밀번호 비교
        List<PwdHistory> pwds = showPwd(userId);
        String newPwd = pwdUpdateDto.getNewPwd();

        boolean newPasswordValid = pwds
                .stream()
                .noneMatch(history -> bCryptPasswordEncoder.matches(newPwd, history.getOldPwd()));

        if(newPasswordValid) {
            // 2. 암호 수정 전에 히스토리 저장
            saveHistory(target, currnetPwd);

            // 암호 수정
            pwdUpdateDto.setNewPwd(bCryptPasswordEncoder.encode(newPwd));
            target.patchPwd(pwdUpdateDto);

            // 3. 새로운 비밀번호로 업데이트
            User updated = userRepository.save(target);

            // 엔티티를 DTO로 변환해서 반환
            return PwdUpdateDto.createResponseDto(updated);
        } else {
            throw new UserException(UserErrorCode.RECENTLY_USED_PASSWORD);
        }
    }

    // 회원정보 수정 시 현재 패스워드 히스토리 테이블에 저장
    @Transactional
    public void saveHistory(User user, String pwd){
        PwdHistory history = new PwdHistory();
        history.setUser(user);
        history.setOldPwd(pwd);
        historyRepository.save(history);
    }

    // 패스워드 최근 3개 조회
    private List<PwdHistory> showPwd(Long userId){
        List<PwdHistory> pwds = historyRepository.findByUserId(userId);
        return pwds;
    }

    // 내 정보 조회
    public UserDetailDto findById(UserDetailsImpl userDetails, Long userId){
        isValidUser(userDetails.getId(), userId);

        return userRepository.findById(userId)
                .map(UserDetailDto::createUserDetailDto)
                .orElse(null);
    }

    // 내가 쓴 게시글 목록
    public Page<PostDetailDto> showMyPost(Long userId, UserDetailsImpl userDetails, Pageable pageable) {
        isValidUser(userDetails.getId(), userId);

        Page<Post> filteredPosts = postRepository.findByUserId(userId, pageable);
        if (filteredPosts.isEmpty()){
            throw new PostException(PostErrorCode.POST_NOT_EXIST);
        }
        return new PageImpl<>(
                filteredPosts.getContent().stream()
                        .map(PostDetailDto::createPostDto)
                        .collect(Collectors.toList()),
                pageable,
                filteredPosts.getTotalElements()
        );
    }

    // 내가 쓴 댓글 목록
    public Page<CommentDetailDto> showMyComment(Long userId, UserDetailsImpl userDetails, Pageable pageable) {
        isValidUser(userDetails.getId(), userId);
        Page<Comment> filteredComments = commentRepository.findByUserId(userId, pageable);
        if (filteredComments.isEmpty()){
            throw new CommentException(CommentErrorCode.NOT_FOUND_COMMENT);
        }
        return new PageImpl<>(
                filteredComments.getContent().stream()
                        .map(CommentDetailDto::createCommentDetailDto)
                        .collect(Collectors.toList()),
                pageable,
                filteredComments.getTotalElements()
        );
    }

    // 내가 팔로우하는 유저 목록
    public Page<UserDetailDto> showMyFollowings(Long userId, UserDetailsImpl userDetails, Pageable pageable) {
        isValidUser(userDetails.getId(), userId);

        // 팔로우 목록
        List<Follow> follows = followRepository.findByFollowerId(userId);

        // 팔로우 목록에서 유저 ID 추출
        List<Long> followingIds = follows.stream()
                .map(Follow::getFollowing)
                .map(User::getUserId)
                .collect(Collectors.toList());

        // 유저 정보 조회 후 리스트 반환

        Page<User> filteredUsers = userRepository.findByUserIdIn(followingIds, pageable);

        if (filteredUsers.isEmpty()){
            throw new FollowException(FollowErrorCode.NOT_FOUND_FOLLOW);
        }
        return new PageImpl<>(
                filteredUsers.getContent().stream()
                        .map(UserDetailDto::createUserDetailDto)
                        .collect(Collectors.toList()),
                pageable,
                filteredUsers.getTotalElements()
        );
    }

    @Override
    public Page<UserDetailDto> showMyFollowers(Long userId, UserDetailsImpl userDetails, Pageable pageable) {
        isValidUser(userDetails.getId(), userId);

        // 팔로워 목록
        List<Follow> followers = followRepository.findByFollowingId(userId);

        // 팔로워 목록에서 유저 ID 추출
        List<Long> followingIds = followers.stream()
                .map(Follow::getFollower)
                .map(User::getUserId)
                .collect(Collectors.toList());

        // 유저 정보 조회 후 리스트 반환

        Page<User> filteredUsers = userRepository.findByUserIdIn(followingIds, pageable);

        if (filteredUsers.isEmpty()){
            throw new FollowException(FollowErrorCode.NOT_FOUND_FOLLOW);
        }
        return new PageImpl<>(
                filteredUsers.getContent().stream()
                        .map(UserDetailDto::createUserDetailDto)
                        .collect(Collectors.toList()),
                pageable,
                filteredUsers.getTotalElements()
        );
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    // 본인 여부 확인
    private void isValidUser(Long targetUserId, Long currentUserId) {
        if (!currentUserId.equals(targetUserId)) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_USER);
        }
    }


}
