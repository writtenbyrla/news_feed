package com.example.news_feed.comment.service;

import com.example.news_feed.comment.dto.request.CommentLikeDto;

public interface CommentLikeService {
    /*
     * 댓글 좋아요 추가
     * @param userId 댓글 좋아요 요청자
     * @param commentId 기존 댓글 정보
     * @return 댓글 좋아요 추가 결과
     */
    CommentLikeDto create(Long commentId, Long userId);

    /*
     * 댓글 좋아요 추가
     * @param userId 댓글 좋아요 요청자
     * @param commentId 기존 댓글 좋아요 정보
     * @return 댓글 좋아요 삭제 결과
     */
    CommentLikeDto delete(Long commentLikeId, Long userId);
}
