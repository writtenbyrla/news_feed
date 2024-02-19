package com.example.news_feed.admin.service;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.UpdatePostDto;

public interface AdminPostService {
    /*
     * 게시글 수정
     * @param postId 기존 게시글 번호
     * @param updatePostDto 게시글 수정 요청정보
     * @return 게시글 수정 결과
     */
    UpdatePostDto update(Long postId, UpdatePostDto updatePostDto);

    /*
     * 게시글 삭제
     * @param postId 기존 게시글 번호
     * @param userId 게시글 삭제 요청자
     * @return 게시글 삭제 결과
     */
    Post delete(Long userId, Long postId);
}
