package com.example.news_feed.admin.service;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;

import java.util.List;

public interface AdminCommentService {

    /*
     * 댓글 수정
     * @param updateCommentDto 댓글 수정 요청정보
     * @param commentId 기존 댓글 정보
     * @return 댓글 수정 결과
     */
    UpdateCommentDto update(Long commentId, UpdateCommentDto updateCommentDto);

    /*
     * 댓글 삭제
     * @param userId 댓글 삭제 요청자
     * @param commentId 기존 댓글 정보
     * @return 댓글 삭제 결과
     */
    Comment delete(Long userId, Long commentId);

    /*
     * 댓글 목록
     * @return 댓글 전체 목록
     */
    List<CommentDetailDto> showAll();

}
