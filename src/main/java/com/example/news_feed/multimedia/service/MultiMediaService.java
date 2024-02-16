package com.example.news_feed.multimedia.service;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.multimedia.dto.MultiMediaDto;
import com.example.news_feed.multimedia.repository.MultiMediaRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultiMediaService {
    private final MultiMediaRepository multiMediaRepository;
    private final UserRepository userRepository;

    // 멀티미디어 등록
    @Transactional
    public MultiMedia uploadFiles(MultiMedia multiMedia) {
        return multiMediaRepository.save(multiMedia);
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
        checkUser(userId);

        // 파일 정보 확인
        MultiMedia target = multiMediaRepository.findById(multiMediaId)
                .orElseThrow(() -> new HttpException(false, "파일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));

        // 작성자 일치 여부 확인
        isWrittenbyUser(userId,target.getPost().getUser().getUserId());

        multiMediaRepository.delete(target);
        return MultiMediaDto.createMultimediaDto(target);
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(false, "유저 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }
    // 본인 작성 여부 확인
    private void isWrittenbyUser(Long userId, Long postUserId) {
        if (!userId.equals(postUserId)) {
            throw new HttpException(false, "본인이 작성한 게시글이 아닙니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
