package com.example.news_feed.post.service.serviceImpl;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.post.service.PostService;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    // 게시글 등록
    @Transactional
    public CreatePostDto createPost(CreatePostDto createPostDto) {
        if (createPostDto.getTitle() == null || createPostDto.getTitle().isEmpty()) {
            throw new PostException(PostErrorCode.NOT_NULL_TITLE);
        }
        if (createPostDto.getContent() == null || createPostDto.getContent().isEmpty()) {
            throw new PostException(PostErrorCode.NOT_NULL_CONTENT);
        }

        // 사용자 정보
        User user = checkUser(createPostDto.getUserId());

        // 엔티티 생성
        Post post = new Post(createPostDto, user);

        // db 저장, DTO로 변경하여 반환
        Post created = postRepository.save(post);
        return CreatePostDto.createPostDto(created);
    }


    // 게시글 수정(기본)
    @Transactional
    public UpdatePostDto update(Long postId, UpdatePostDto updatePostDto) {
        // 기존 유저정보 조회 및 예외 처리
        User user = checkUser(updatePostDto.getUserId());
        // 수정하고자 하는 게시글 정보 조회
        Post target = checkPost(postId);
        // 유저인 경우에만 작성자 본인 여부 확인(관리자는 수정 가능)
        if(user.getRole().getAuthority().equals("USER")){
            // 작성자 여부 확인
            isWrittenByUser(target.getUser().getUserId(), updatePostDto.getUserId());
        }

        // 게시글 수정
        target.patch(updatePostDto);

        // 엔티티 db 저장
        Post updated = postRepository.save(target);

        // dto로 변환하여 반환
        return UpdatePostDto.updatePostDto(updated);
    }


    @Transactional
    // 게시글 삭제
    public Post delete(Long userId, Long postId) {
        // 기존 유저정보 조회 및 예외 처리
        User user = checkUser(userId);

        // 게시글 확인
        Post target = checkPost(postId);

        // 유저인 경우에만 작성자 본인 여부 확인(관리자는 수정 가능)
        if(user.getRole().getAuthority().equals("USER")){
            // 작성자 여부 확인
            isWrittenByUser(target.getUser().getUserId(), userId);
        }

        postRepository.delete(target);
        return target;
    }

    // 게시글 목록
    public Page<PostDetailDto> showAll(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        return new PageImpl<>(
                posts.getContent().stream()
                        .map(PostDetailDto::createPostDto)
                        .collect(Collectors.toList()),
                pageable,
                posts.getTotalElements()
        );
    }

    // 게시글 상세
    public PostDetailDto show(Long postId) {
        return postRepository.findById(postId)
                .map(PostDetailDto::createPostDto)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
    }

    // 게시글 목록(제목, 내용 검색)
    public Page<PostDetailDto> findByOption(String keyword, Pageable pageable) {

        Page<Post> filteredPosts = postRepository.findByOption(keyword, pageable);
        if (filteredPosts == null || filteredPosts.isEmpty()) {
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



    // 게시글 목록(username으로 검색)
    public Page<PostDetailDto> findByUser(String username, Pageable pageable) {
        Page<Post> filteredPosts = postRepository.findByUser(username, pageable);
        if (filteredPosts==null || filteredPosts.isEmpty()){
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

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    // 게시글 정보 확인
    private Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
    }

    // 본인 작성 여부 확인
    private void isWrittenByUser(Long userId, Long postUserId) {
        if (!userId.equals(postUserId)) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_USER);
        }
    }

}
