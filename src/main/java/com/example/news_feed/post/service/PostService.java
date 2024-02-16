package com.example.news_feed.post.service;

import com.example.news_feed.common.aws.FileUploadService;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.multimedia.repository.MultiMediaRepository;
import com.example.news_feed.multimedia.service.MultiMediaService;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final UserRepository userRepository;
    private final MultiMediaRepository multiMediaRepository;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final MultiMediaService multiMediaService;


    // 게시글 등록(첨부파일 없을때)
    @Transactional
    public CreatePostDto createPost(CreatePostDto createPostDto) {

        // 사용자 정보
        User user = checkUser(createPostDto.getUserId());

        // 엔티티 생성
        Post post = new Post(createPostDto, user);

        // db 저장, DTO로 변경하여 반환
        Post created = postRepository.save(post);
        return CreatePostDto.createPostDto(created);
    }

    // 게시글 등록(첨부파일 있을 때)
    @Transactional
    public CreatePostDto createWithFile(List<MultipartFile> files, CreatePostDto createPostDto) {

        // 사용자 정보
        User user = checkUser(createPostDto.getUserId());

        // 엔티티 생성
        Post post = new Post(createPostDto, user);

        // 파일 s3 업로드, db 저장
        List<String> fileUrls = fileUploadService.uploadFiles(files);
        for (String url : fileUrls) {
            MultiMedia multiMedia = new MultiMedia(url, post);
            multiMediaService.uploadFiles(multiMedia);
        }

        // db 저장, DTO로 변경하여 반환
        Post created = postRepository.save(post);
        return CreatePostDto.createPostDto(created);
    }

    // 게시글 수정(기본)
    @Transactional
    public UpdatePostDto update(Long postId, UpdatePostDto updatePostDto) {

        updatePostDto.setPostId(postId);

        // 기존 유저정보 조회 및 예외 처리
        checkUser(updatePostDto.getUserId());
        // 수정하고자 하는 게시글 정보 조회
        Post target = checkPost(postId);
        // 작성자 여부 확인
        isWrittenbyUser(updatePostDto.getUserId(), target.getUser().getUserId());

        // 게시글 수정
        target.patch(updatePostDto);

        // 엔티티 db 저장
        Post updated = postRepository.save(target);

        // dto로 변환하여 반환
        return UpdatePostDto.updatePostDto(updated);
    }

    // 게시글 수정(첨부파일 있을 때)

    /*
     * 게시글 수정
     * 1. 기존 유저정보 조회, 게시글 정보 조회(target), 작성자 여부 확인
     * 2. 게시글 id(target)로 기존 멀티미디어 db에서 삭제
     * 3. 멀티미디어 s3 등록, db 등록
     * 4. 게시글 수정
     * */
    @Transactional
    @Modifying
    public UpdatePostDto updateWithFile(Long postId, UpdatePostDto updatePostDto, List<MultipartFile> files) {

        updatePostDto.setPostId(postId);

        // 1.
        // 기존 유저정보 조회 및 예외 처리
        checkUser(updatePostDto.getUserId());
        // 수정하고자 하는 게시글 정보 조회
        Post target = checkPost(postId);
        // 작성자 여부 확인
        isWrittenbyUser(updatePostDto.getUserId(), target.getUser().getUserId());

        // 2. 기존 멀티미디어 파일 db에서만 삭제
        List<MultiMedia> currentFiles = multiMediaRepository.findByPostId(postId);
        if (currentFiles != null){
            multiMediaRepository.deleteByPostId(target.getPostId());
        }

        // 3. 파일 s3 업로드, db 저장
            List<String> fileUrls = fileUploadService.uploadFiles(files);
            for (String url : fileUrls) {
                MultiMedia multiMedia = new MultiMedia(url, target);
                multiMediaService.uploadFiles(multiMedia);
            }

        // 4. 게시글 수정
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
        checkUser(userId);

        // 게시글 확인
        Post target = checkPost(postId);

        // 작성자 일치 여부 확인
        isWrittenbyUser(userId,target.getUser().getUserId());

        postRepository.delete(target);
        return target;
    }

    // 게시글 목록
    public List<PostDetailDto> showAll() {
        return postRepository.findAll()
                .stream()
                .map(PostDetailDto::createPostDto)
                .collect(Collectors.toList());

    }

    // 게시글 상세
    public PostDetailDto show(Long postId) {
        return postRepository.findById(postId)
                .map(PostDetailDto::createPostDto)
                .orElse(null);
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(false, "유저 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    // 게시글 정보 확인
    private Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new HttpException(false, "게시글 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    // 본인 작성 여부 확인
    private void isWrittenbyUser(Long userId, Long postUserId) {
        if (!userId.equals(postUserId)) {
            throw new HttpException(false, "본인이 작성한 게시글이 아닙니다.", HttpStatus.BAD_REQUEST);
        }
    }


}
