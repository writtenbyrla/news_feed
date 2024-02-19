package com.example.news_feed.multimedia.serviceImpl;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.common.aws.FileUploadService;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.multimedia.dto.MultiMediaDto;
import com.example.news_feed.multimedia.exception.MultiMediaErrorCode;
import com.example.news_feed.multimedia.exception.MultiMediaException;
import com.example.news_feed.multimedia.repository.MultiMediaRepository;
import com.example.news_feed.multimedia.service.MultiMediaService;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultiMediaServiceImpl implements MultiMediaService {
    private final MultiMediaRepository multiMediaRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;


    // 멀티미디어 등록
    @Transactional
    public void createFile(List<MultipartFile> files, Long postId) {

        Post post = checkPost(postId);

        // 파일 s3 업로드, db 저장
        List<String> fileUrls = fileUploadService.uploadFiles(files);
        for (String url : fileUrls) {
            MultiMedia multiMedia = new MultiMedia(url, post);
            multiMediaRepository.save(multiMedia);
        }
    }

    /*
     * 멀티미디어 수정
     * 1. 기존 유저정보 조회, 게시글 정보 조회(target), 작성자 여부 확인
     * 2. 게시글 id(target)로 기존 멀티미디어 db에서 삭제
     * 3. 멀티미디어 s3 등록, db 등록
     * */
    @Transactional
    @Modifying
    public void updateFile(UserDetailsImpl userDetails, Long postId, List<MultipartFile> files) {

        // 1.
        // 수정하고자 하는 게시글 정보 조회
        Post post = checkPost(postId);
        // 기존 유저정보 조회 및 예외 처리
        User user = checkUser(post.getUser().getUserId());
        // 유저인 경우에만 작성자 본인 여부 확인(관리자는 수정 가능)
        if(user.getRole().getAuthority().equals("USER")){
            // 작성자 여부 확인
            isWrittenByUser(post.getUser().getUserId(), userDetails.getId());
        }

        // 2. 기존 멀티미디어 파일 db에서만 삭제
        List<MultiMediaDto> currentFiles = showFiles(postId);
        if (currentFiles != null){
            multiMediaRepository.deleteByPostId(post.getPostId());
        }

        // 3. 파일 s3 업로드, db 저장
        List<String> fileUrls = fileUploadService.uploadFiles(files);
        for (String url : fileUrls) {
            MultiMedia multiMedia = new MultiMedia(url, post);
            multiMediaRepository.save(multiMedia);
        }
    }

    // 게시글의 멀티미디어 목록 보기
    public List<MultiMediaDto> showFiles(Long postId){
        return multiMediaRepository.findByPostId(postId)
                .stream()
                .map(MultiMediaDto::createMultimediaDto)
                .collect(Collectors.toList());
    }

    // 멀티미디어 삭제
    @Transactional
    public MultiMediaDto deleteFiles(Long userId, Long multiMediaId){
        // 기존 유저정보 조회 및 예외 처리
        User user = checkUser(userId);

        // 파일 정보 확인
        MultiMedia target = multiMediaRepository.findById(multiMediaId)
                .orElseThrow(() -> new MultiMediaException(MultiMediaErrorCode.NOT_FOUND_FILE));

        // 유저인 경우에만 작성자 본인 여부 확인(관리자는 수정 가능)
        if(user.getRole().getAuthority().equals("USER")){
            // 작성자 여부 확인
            isWrittenByUser(userId,target.getPost().getUser().getUserId());
        }

        multiMediaRepository.delete(target);
        return MultiMediaDto.createMultimediaDto(target);
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    // 본인 작성 여부 확인
    private void isWrittenByUser(Long userId, Long postUserId) {
        if (!userId.equals(postUserId)) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_USER);
        }
    }

    // 게시글 정보 확인
    private Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
    }
}
