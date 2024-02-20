package com.example.news_feed.comment.service;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    /*
     * 댓글 추가
     * @param createCommentDto 댓글 추가 요청정보
     * @param postId 기존 게시글 정보
     * @return 댓글 추가 결과
     */
    CreateCommentDto create(Long postId, CreateCommentDto createCommentDto);

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
     * @param postId 기존 게시글 정보
     * @return 해당 게시물의 댓글 목록
     */
    Page<CommentDetailDto> showAll(Long postId, Pageable pageable);

    /*
     * 댓글 상세
     * @param commentId 기존 댓글 정보
     * @return 댓글 상세 정보
     */
    CommentDetailDto show(Long commentId);
}
