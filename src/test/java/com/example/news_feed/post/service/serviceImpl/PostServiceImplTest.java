package com.example.news_feed.post.service.serviceImpl;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @InjectMocks
    PostServiceImpl postServiceImpl;

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("게시글 작성")
    class CreatePost{
        @DisplayName("게시글 작성 성공")
        @Test
        void create_post_ok() {
            // given
            // user 정보 세팅
            User user = TestUtil.createUser(1L);
            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
            var post = CreatePostDto.builder().title("제목").content("내용").userId(user.getUserId()).build();
            given(postRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

            // when
            CreatePostDto savedPost = postServiceImpl.createPost(post);

            // then
            assertNotNull(savedPost);
            assertEquals("제목", savedPost.getTitle());
            assertEquals("내용", savedPost.getContent());

            verify(postRepository, times(1)).save(any());
        }

        @DisplayName("게시글 작성 실패 - 유저 정보 없음")
        @Test
        void create_post_fail_not_found_user() {
            CreatePostDto post = CreatePostDto.builder().title("제목").content("내용").userId(100L).build();

            // 사용자가 없는 상황을 가정하여 예외 처리
            when(userRepository.findById(post.getUserId())).thenReturn(Optional.empty());

            assertThrows(UserException.class, () -> {
                postServiceImpl.createPost(post);
            }, "등록된 사용자가 없습니다.");
        }

        @DisplayName("게시글 작성 실패 - 내용 없음")
        @Test
        void create_post_fail_non_content() {
            User user = TestUtil.createUser(1L);
            var post = CreatePostDto.builder().title("제목").userId(user.getUserId()).build();

            assertThrows(PostException.class, () -> {
                postServiceImpl.createPost(post);
            },"내용을 입력해주세요.");
        }

        @DisplayName("게시글 작성 실패 - 제목 없음")
        @Test
        void create_post_fail_non_title() {
            User user = TestUtil.createUser(1L);
            var post = CreatePostDto.builder().content("내용").userId(user.getUserId()).build();

            assertThrows(PostException.class, () -> {
                postServiceImpl.createPost(post);
            },"제목을 입력해주세요.");
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {
        @DisplayName("게시글 수정 성공")
        @Test
        void update_post_ok() {
            // given
            Post post = TestUtil.createPost(1L, 1L);
            User user = post.getUser();

            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
            given(postRepository.findById(post.getPostId())).willReturn(Optional.of(post));

            var updatePost = UpdatePostDto.builder()
                    .title("수정222")
                    .content("수정222")
                    .userId(user.getUserId())
                    .build();

            given(postRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

            // when
            UpdatePostDto updatedPost = postServiceImpl.update(1L, updatePost);

            // then
            assertNotNull(updatedPost);
            assertEquals("수정222", updatedPost.getTitle());
            assertEquals("수정222", updatedPost.getContent());

            verify(postRepository, times(1)).save(any());
        }

        @DisplayName("게시글 수정 성공 - 제목만")
        @Test
        void update_post_ok_only_title() {
            // given
            Post post = TestUtil.createPost(1L, 1L);
            User user = post.getUser();

            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
            given(postRepository.findById(post.getPostId())).willReturn(Optional.of(post));

            var updatePost = UpdatePostDto.builder()
                    .title("수정222")
                    .userId(user.getUserId())
                    .build();

            given(postRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

            // when
            UpdatePostDto updatedPost = postServiceImpl.update(1L, updatePost);

            // then
            assertNotNull(updatedPost);
            assertEquals("수정222", updatedPost.getTitle());

            verify(postRepository, times(1)).save(any());
        }

        @DisplayName("게시글 수정 성공 - 내용만")
        @Test
        void update_post_ok_only_content() {
            // given
            Post post = TestUtil.createPost(1L, 1L);
            User user = post.getUser();

            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
            given(postRepository.findById(post.getPostId())).willReturn(Optional.of(post));

            var updatePost = UpdatePostDto.builder()
                    .content("수정222")
                    .userId(user.getUserId())
                    .build();

            given(postRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

            // when
            UpdatePostDto updatedPost = postServiceImpl.update(1L, updatePost);

            // then
            assertNotNull(updatedPost);
            assertEquals("수정222", updatedPost.getContent());

            verify(postRepository, times(1)).save(any());
        }

        @DisplayName("게시글 수정 실패 - 게시글 정보 없음")
        @Test
        void update_post_fail_not_found_post() {
            // given
            Post post = TestUtil.createPost(1L, 1L);
            User user = post.getUser();

            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));

            var updatePost = UpdatePostDto.builder()
                    .title("수정222")
                    .content("수정222")
                    .userId(user.getUserId())
                    .build();

            // then
            assertThrows(PostException.class, () -> {
                postServiceImpl.update(100L, updatePost);
            },"게시글 정보가 없습니다.");
        }

        @DisplayName("게시글 수정 실패 - 본인 아님")
        @Test
        void update_post_fail_unauthorized_user() {
            // given
            Post post = TestUtil.createPost(1L, 1L);

            var updatePost = UpdatePostDto.builder()
                    .title("수정222")
                    .content("수정222")
                    .userId(100L)
                    .build();

            // then
            assertThrows(UserException.class, () -> {
                postServiceImpl.update(1L, updatePost);
            }, "본인이 아닙니다.");
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost{
        @DisplayName("게시글 삭제 성공")
        @Test
        void delete_post_ok(){
            // given
            Post post = TestUtil.createPost(1L, 1L);
            User user = post.getUser();

            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
            given(postRepository.findById(post.getPostId())).willReturn(Optional.of(post));

            // when
            Post deletedPost = postServiceImpl.delete(user.getUserId(), post.getPostId());

            // then
            assertNotNull(deletedPost);
            assertEquals(post.getTitle(), deletedPost.getTitle());
            assertEquals(post.getContent(), deletedPost.getContent());

            verify(postRepository, times(1)).delete(any());
        }
        @DisplayName("게시글 삭제 실패 - 게시글 정보 없음")
        @Test
        void delete_post_fail_not_found_post(){
            // given
            Post post = TestUtil.createPost(1L, 1L);
            User user = post.getUser();

            given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));

            // then
            assertThrows(PostException.class, () -> {
                postServiceImpl.delete(user.getUserId(), 100L);
            },"게시글 정보가 없습니다.");
        }

        @DisplayName("게시글 삭제 실패 - 본인 아님")
        @Test
        void delete_post_fail_unathorized_user(){
            // given
            TestUtil.createPost(1L, 1L);

            // then
            assertThrows(UserException.class, () -> {
                postServiceImpl.delete(100L, 1L);
            },"본인이 아닙니다.");
        }
    }

    @Nested
    @DisplayName("게시글 목록")
    class PostList {
        @DisplayName("게시글 목록 성공")
        @Test
        void post_list_ok(){
            // given
            Post post1 = TestUtil.createPost(1L, 1L);
            Post post2 = TestUtil.createPost(1L, 2L);
            Post post3 = TestUtil.createPost(1L, 3L);

            // Posts 페이지 생성
            List<Post> postContent = List.of(post1, post2, post3); // 원하는 만큼의 게시물 추가
            Page<Post> postsPage = new PageImpl<>(postContent, PageRequest.of(0, 10), postContent.size());
            given(postRepository.findAll(any(Pageable.class))).willReturn(postsPage);

            // when
            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<PostDetailDto> postList = postServiceImpl.showAll(pageable);

            // then
            assertNotNull(postList);
            assertEquals(3, postList.getTotalElements());
            assertEquals("제목1", postList.getContent().get(0).getTitle());
            assertEquals("제목2", postList.getContent().get(1).getTitle());
            assertEquals("제목3", postList.getContent().get(2).getTitle());
        }

        @DisplayName("게시글 상세 성공")
        @Test
        void post_detail_ok() {
            // given
            Post post1 = TestUtil.createPost(1L, 1L);
            Post post2 = TestUtil.createPost(1L, 2L);
            Post post3 = TestUtil.createPost(1L, 3L);

            // Posts 페이지 생성
            List<Post> postContent = List.of(post1, post2, post3); // 원하는 만큼의 게시물 추가
            Page<Post> postsPage = new PageImpl<>(postContent, PageRequest.of(0, 10), postContent.size());
            given(postRepository.findAll(any(Pageable.class))).willReturn(postsPage);

            // when
            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<PostDetailDto> postList = postServiceImpl.showAll(pageable);

            // then
            assertNotNull(postList);
            assertEquals(3, postList.getTotalElements());
            assertEquals("제목1", postList.getContent().get(0).getTitle());
            assertEquals("제목2", postList.getContent().get(1).getTitle());
            assertEquals("제목3", postList.getContent().get(2).getTitle());
        }

        @DisplayName("게시글 상세 실패 - 게시글 정보 없음")
        @Test
        void post_detail_fail_not_found_post(){
            // given
            Post post = TestUtil.createPost(1L, 1L);

            // then
            assertThrows(PostException.class, () -> {
                postServiceImpl.show(100L);
            },"게시글 정보가 없습니다.");
        }
    }

    @Nested
    @DisplayName("게시글 검색")
    class Search{
        @DisplayName("게시글 키워드 검색 성공")
        @Test
        void post_search_by_keyword_ok(){
            Post post1 = TestUtil.createPost(1L, 1L);
            Post post2 = TestUtil.createPost(1L, 2L);
            Post post3 = TestUtil.createPost(1L, 3L);

            // 키워드로 검색된 결과를 가정하여 반환하도록 Mock 설정
            List<Post> searchResult = List.of(post1, post2, post3);
            Page<Post> searchPage = new PageImpl<>(searchResult);
            when(postRepository.findByOption(eq("제목"), any(Pageable.class))).thenReturn(searchPage);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<PostDetailDto> postList = postServiceImpl.findByOption("제목", pageable);

            assertNotNull(postList);
            assertEquals(3, postList.getTotalElements());
            assertEquals("제목1", postList.getContent().get(0).getTitle());
            assertEquals("제목2", postList.getContent().get(1).getTitle());
            assertEquals("제목3", postList.getContent().get(2).getTitle());
        }

        @DisplayName("게시글 키워드 검색 실패 - 게시글 없음")
        @Test
        void post_search_by_keyword_fail(){
            TestUtil.createPost(1L, 1L);
            TestUtil.createPost(1L, 2L);
            TestUtil.createPost(1L, 3L);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            assertThrows(PostException.class, () -> {
                postServiceImpl.findByOption("jidong", pageable);
            },"게시글 정보가 없습니다.");
        }

        @DisplayName("게시글 작성자 검색 성공")
        @Test
        void post_search_by_username_ok(){
            Post post1 = TestUtil.createPost(1L, 1L);
            Post post2 = TestUtil.createPost(1L, 2L);
            Post post3 = TestUtil.createPost(1L, 3L);

            // 키워드로 검색된 결과를 가정하여 반환하도록 Mock 설정
            List<Post> searchResult = List.of(post1, post2, post3);
            Page<Post> searchPage = new PageImpl<>(searchResult);
            when(postRepository.findByUser(eq("jidong"), any(Pageable.class))).thenReturn(searchPage);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<PostDetailDto> postList = postServiceImpl.findByUser("jidong", pageable);

            assertNotNull(postList);
            assertEquals(3, postList.getTotalElements());
            assertEquals("제목1", postList.getContent().get(0).getTitle());
            assertEquals("제목2", postList.getContent().get(1).getTitle());
            assertEquals("제목3", postList.getContent().get(2).getTitle());
        }

        @DisplayName("게시글 키워드 검색 실패 - 게시글 없음")
        @Test
        void post_search_by_username_fail(){
            TestUtil.createPost(1L, 1L);
            TestUtil.createPost(1L, 2L);
            TestUtil.createPost(1L, 3L);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            assertThrows(PostException.class, () -> {
                postServiceImpl.findByUser("user", pageable);
            },"게시글 정보가 없습니다.");
        }
    }

    static class TestUtil{
        // 유저 생성만 필요한 경우
        public static User createUser(Long userId) {
            User user = new User("jidong", "asdf123!", "jidong@gmail.com");
            ReflectionTestUtils.setField(user, "userId", userId);
            ReflectionTestUtils.setField(user, "role", UserRoleEnum.USER);
            return user;
        }

        // 유저, 게시글 생성 동시에 필요한 경우
        public static Post createPost(Long userId, Long postId) {
            User user = new User("jidong", "asdf123!", "jidong@gmail.com");
            ReflectionTestUtils.setField(user, "userId", userId);
            ReflectionTestUtils.setField(user, "role", UserRoleEnum.USER);

            Post post = new Post("제목"+postId, "내용", user);
            ReflectionTestUtils.setField(post, "postId", postId);
            return post;
        }
    }

}