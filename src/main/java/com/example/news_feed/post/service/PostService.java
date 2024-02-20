package com.example.news_feed.post.service;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;

import java.util.List;

public interface PostService {

    /*
     * 게시글 등록
     * @param createPostDto 게시글 생성 요청정보
     * @return 게시글 생성 결과
     */
    CreatePostDto createPost(CreatePostDto createPostDto);

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

    /*
     * 게시글 목록
     * @return 게시글 전체 목록
     */
    List<PostDetailDto> showAll();

    /*
     * 게시글 상세
     * @param postId 기존 게시글 번호
     * @return 게시글 정보
     */
    PostDetailDto show(Long postId);

    /*
     * 게시글 검색(keyword -> 제목 또는 내용)
     * @param title 기존 게시글 번호
     * @return 게시글 정보
     */
    List<PostDetailDto> findBySearchOption(String keyword);

    /*
     * 게시글 검색(username)
     * @param username 작성자 유저네임
     * @return 게시글 정보
     */
    List<PostDetailDto> findByUser(String username);
}
