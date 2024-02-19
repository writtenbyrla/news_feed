package com.example.news_feed.multimedia.service;

import com.example.news_feed.multimedia.dto.MultiMediaDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MultiMediaService {
    /*
     * 멀티미디어 추가
     * @param files 멀티미디어 생성 요청정보
     * @param postId 게시글 번호
     * @return 멀티미디어 생성 결과
     */
    void createFile(List<MultipartFile> files, Long postId);

    /*
     * 멀티미디어 수정
     * @param files 멀티미디어 수정 요청정보
     * @param postId 게시글 번호
     * @return 멀티미디어 수정 결과
     */
    void updateFile(Long postId, List<MultipartFile> files);

    /*
     * 멀티미디어 목록
     * @param postId 게시글 번호
     * @return 멀티미디어 목록
     */
    List<MultiMediaDto> showFiles(Long postId);

    /*
     * 멀티미디어 삭제
     * @param userId 멀티미디어 삭제 요청자
     * @param multiMediaId 멀티미디어 번호
     * @return 멀티미디어 삭제 결과
     */
    MultiMediaDto deleteFiles(Long userId, Long multiMediaId);
}
