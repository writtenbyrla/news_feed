package com.example.news_feed.post.service;

import com.example.news_feed.post.dto.request.PostLikeDto;

public interface PostLikeService {

    /*
     * 좋아요 등록
     * @param postId 기존 게시글 번호
     * @param userId 좋아요 요청자
     * @return 게시글 좋아요 결과
     */
    PostLikeDto create(Long postId, Long userId);

    /*
     * 좋아요 취소
     * @param postLikeId 기존 게시글 좋아요 번호
     * @param userId 좋아요 요청자
     * @return 게시글 취소 결과
     */
    PostLikeDto delete(Long userId, Long postLikeId);




}
