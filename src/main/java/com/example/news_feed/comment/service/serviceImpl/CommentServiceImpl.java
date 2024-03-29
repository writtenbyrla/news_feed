package com.example.news_feed.comment.service.serviceImpl;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.comment.exception.CommentErrorCode;
import com.example.news_feed.comment.exception.CommentException;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.service.CommentService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 작성
    @Transactional
    public CreateCommentDto create(Long postId, CreateCommentDto createCommentDto) {

        // 사용자 정보
        User user = checkUser(createCommentDto.getUserId());

        // 게시글 정보
        Post post = checkPost(postId);

        // 엔티티 생성
        Comment comment = new Comment(createCommentDto, post, user);

        // 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        // Dto로 변경하여 반환
        return CreateCommentDto.createCommentDto(created);
    }

    // 댓글 수정
    @Transactional
    public UpdateCommentDto update(Long commentId, UpdateCommentDto updateCommentDto) {

        // 사용자 정보
        User user = checkUser(updateCommentDto.getUserId());

        // 댓글 정보
        Comment target = checkComment(commentId);

        // 유저인 경우에만 작성자 본인 여부 확인(관리자는 수정 가능)
        if(user.getRole().getAuthority().equals("USER")) {
            // 작성자가 일치할 경우에만 수정 가능
            isWrittenByUser(updateCommentDto.getUserId(), target.getUser().getUserId());
        }
        // 댓글 수정
        target.patch(commentId, updateCommentDto);

        // DB로 갱신
        Comment updated = commentRepository.save(target);

        // 댓글 엔티티를 DTO로 변환 및 반환
        return UpdateCommentDto.updateCommentDto(updated);

    }

    // 댓글 삭제
    @Transactional
    public Comment delete(Long userId, Long commentId) {
        // 기존 유저정보 조회 및 예외 처리
        User user = checkUser(userId);

        // 댓글 정보
        Comment target = checkComment(commentId);
        if(user.getRole().getAuthority().equals("USER")) {
            // 작성자가 일치할 경우에만 삭제 가능
            isWrittenByUser(userId, target.getUser().getUserId());
        }

        commentRepository.delete(target);
        return target;
    }

    // 댓글 목록
    public Page<CommentDetailDto> showAll(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        return new PageImpl<>(
                comments.getContent().stream()
                        .map(CommentDetailDto::createCommentDetailDto)
                        .collect(Collectors.toList()),
                pageable,
                comments.getTotalElements()
        );

    }

    // 댓글 상세 보기
    public CommentDetailDto show(Long commentId) {

        return commentRepository.findById(commentId)
                .map(CommentDetailDto::createCommentDetailDto)
                .orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT));

    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    // 게시글 정보 확인
    private Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
    }

    // 댓글 정보 확인
    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT));
    }

    // 댓글 작성자 확인
    private void isWrittenByUser(Long userId, Long commentUserId) {
        if (!userId.equals(commentUserId)) {
            throw new UserException(UserErrorCode.UNAUTHORIZED_USER);
        }
    }


}
